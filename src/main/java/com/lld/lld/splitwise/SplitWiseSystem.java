package com.lld.lld.splitwise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.lld.lld.splitwise.concurency.BalanceSheetLockManager;
import com.lld.lld.splitwise.enums.SplitMethod;
import com.lld.lld.splitwise.models.BalanceSheet;
import com.lld.lld.splitwise.models.Expense;
import com.lld.lld.splitwise.models.Group;
import com.lld.lld.splitwise.models.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SplitWiseSystem {
    private static volatile SplitWiseSystem instance;
    private List<User> users;
    private List<Group> groups;
    private final BalanceSheetLockManager balanceSheetLockManager = new BalanceSheetLockManager();

    public static synchronized SplitWiseSystem getInstance() {
        if(instance == null) {
            instance = new SplitWiseSystem();
        }
        return instance;
    }

    public User createUser(String name, String email) {
        if (users == null) {
            users = new CopyOnWriteArrayList<>();
        }
        User newUser = new User(name, email);
        users.add(newUser);
        return newUser;
    }

    public Group createGroup(String name, List<User> members) {
        if (groups == null) {
            groups = new CopyOnWriteArrayList<>();
        }
        
        Map<User, Map<User, Double>> balanceMap = new HashMap<>();
        
        for (User user : members) {
            Map<User, Double> userBalance = new HashMap<>();
            for (User otherUser : members) {
                if (!user.equals(otherUser)) {
                    userBalance.put(otherUser, 0.0);
                }
            }
            balanceMap.put(user, userBalance);
        }
        
        BalanceSheet balanceSheet = new BalanceSheet(balanceMap);
        Group newGroup = new Group(name, balanceSheet, new ArrayList<>(members));
        groups.add(newGroup);
        return newGroup;
    }

    public void addExpenseInGroup(Group group,
                              String description,
                              User paidByUser,
                              Double amount,
                              List<User> splitBetweenUsers,
                              SplitMethod splitMethod,
                              Map<User, Double> distribution) throws InterruptedException {

        // Attempt to acquire the lock for this group
        boolean locked = balanceSheetLockManager.lockBalanceSheet(group, paidByUser);

        if (!locked) {
            // Someone else already holds it → throw immediately
            throw new RuntimeException("Group " + group.getName() + "'s balance sheet is occupied right now.");
        }

        try {
            // Simulate some delay to make concurrency visible
            Thread.sleep(300);

            // Perform the expense addition
            Expense newExpense = Expense.builder()
                    .description(description)
                    .amount(amount)
                    .splitBetweenUsers(splitBetweenUsers)
                    .splitMethod(splitMethod)
                    .paidByUser(paidByUser)
                    .distribution(distribution)
                    .build();

            group.addExpense(newExpense);

        } finally {
            // Release lock only if this thread actually acquired it
            balanceSheetLockManager.releaseLock(group);
        }
    }
 
}

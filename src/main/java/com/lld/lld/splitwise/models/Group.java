package com.lld.lld.splitwise.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Group {
    private String name;
    private BalanceSheet balanceSheet;
    private List<User> members;

    public void addUser(User user) {
        this.members.add(user);
    }

    public void removeUser(User user) {
        this.members.remove(user);
    }

    public void addExpense(Expense expense) {
        balanceSheet.updateBalance(expense);
    }

    public void settleBalance() {

    }
}

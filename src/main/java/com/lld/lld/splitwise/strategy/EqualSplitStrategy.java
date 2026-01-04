package com.lld.lld.splitwise.strategy;

import java.util.Map;

import com.lld.lld.splitwise.models.Expense;
import com.lld.lld.splitwise.models.User;

public class EqualSplitStrategy implements SplitStrategy {

    @Override
    public void split(Expense expense, Map<User, Map<User, Double>> balance) {
        
        Double totalAmount = expense.getAmount();
        Integer distributedBetween = expense.getSplitBetweenUsers().size() + 1;

        Double individualAmount = totalAmount / distributedBetween;

        Map<User, Double> userBalance = balance.get(expense.getPaidByUser());
        for (Map.Entry<User, Double> userBalanceGiver: userBalance.entrySet()) {
            User givenMoneyToUser = userBalanceGiver.getKey();
            if(expense.getSplitBetweenUsers().contains(givenMoneyToUser)) {
                userBalance.put(givenMoneyToUser, userBalanceGiver.getValue() + individualAmount);
            }
        }

        for(User takenMoneyByUser: expense.getSplitBetweenUsers()) {
            Map<User, Double> userBalanceTaker = balance.get(takenMoneyByUser);

            User takenFromUser = expense.getPaidByUser();

            userBalanceTaker.put(takenFromUser, userBalance.getOrDefault(takenFromUser, 0.0) - individualAmount);
        }
        
    }
    
}

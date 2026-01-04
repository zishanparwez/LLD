package com.lld.lld.splitwise.strategy;

import java.util.Map;

import com.lld.lld.splitwise.models.Expense;
import com.lld.lld.splitwise.models.User;

public class PercentageSplitStrategy implements SplitStrategy {

    @Override
    public void split(Expense expense, Map<User, Map<User, Double>> balance) {
        Double totalAmount = expense.getAmount();
        User paidBy = expense.getPaidByUser();
        Map<User, Double> distribution = expense.getDistribution();
        
        // Update balance for the person who paid
        Map<User, Double> payerBalance = balance.get(paidBy);
        for (User user : expense.getSplitBetweenUsers()) {
            Double percentage = distribution.get(user);
            Double amount = (percentage / 100.0) * totalAmount;
            payerBalance.put(user, payerBalance.getOrDefault(user, 0.0) + amount);
        }
        
        // Update balance for people who owe money
        for (User user : expense.getSplitBetweenUsers()) {
            Map<User, Double> userBalance = balance.get(user);
            Double percentage = distribution.get(user);
            Double amount = (percentage / 100.0) * totalAmount;
            userBalance.put(paidBy, userBalance.getOrDefault(paidBy, 0.0) - amount);
        }
    }
    
}

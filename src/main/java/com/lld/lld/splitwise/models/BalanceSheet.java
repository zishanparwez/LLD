package com.lld.lld.splitwise.models;

import java.util.Map;

import com.lld.lld.splitwise.factory.SplitStrategyFactory;
import com.lld.lld.splitwise.strategy.SplitStrategy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BalanceSheet {
    private Map<User, Map<User, Double>> balance;
    private SplitStrategy splitStrategy;

    public BalanceSheet(Map<User, Map<User, Double>> balance) {
        this.balance = balance;
    }

    public void updateBalance(Expense expense) {
        this.splitStrategy = SplitStrategyFactory.getFactoryInstance().createSplitFactory(expense.getSplitMethod());
        splitStrategy.split(expense, balance);
    }

    public void showBalance() {
        for (Map.Entry<User, Map<User, Double>> outerEntry : balance.entrySet()) {
            User user = outerEntry.getKey(); // main user (payer)
            Map<User, Double> userBalance = outerEntry.getValue();
        
            for (Map.Entry<User, Double> innerEntry : userBalance.entrySet()) {
                User owesTo = innerEntry.getKey();    // receiver
                Double amount = innerEntry.getValue(); // amount owed
        
                System.out.println(user.getName() + " owes " + owesTo.getName() + " : " + amount);
            }
        }
    }

    public void showUserBalance(User user) {
        Map<User, Double> userBalance = balance.get(user);
    
        for (Map.Entry<User, Double> innerEntry : userBalance.entrySet()) {
            User owesTo = innerEntry.getKey();    // receiver
            Double amount = innerEntry.getValue(); // amount owed
    
            System.out.println(user.getName() + " owes " + owesTo.getName() + " : " + amount);
        }
    }
}

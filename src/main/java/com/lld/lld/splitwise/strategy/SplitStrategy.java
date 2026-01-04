package com.lld.lld.splitwise.strategy;

import java.util.Map;

import com.lld.lld.splitwise.models.Expense;
import com.lld.lld.splitwise.models.User;

public interface SplitStrategy {
    public void split(Expense expense, Map<User, Map<User, Double>> balance);
}

package com.lld.lld.splitwise.models;

import java.util.List;
import java.util.Map;

import com.lld.lld.splitwise.enums.SplitMethod;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Expense {
    private String description;
    private User paidByUser;
    private Double amount;
    private List<User> splitBetweenUsers;
    private SplitMethod splitMethod;
    private Map<User, Double> distribution;
}

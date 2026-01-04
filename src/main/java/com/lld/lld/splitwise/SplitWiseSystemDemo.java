package com.lld.lld.splitwise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lld.lld.splitwise.enums.SplitMethod;
import com.lld.lld.splitwise.models.Group;
import com.lld.lld.splitwise.models.User;

public class SplitWiseSystemDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== SplitWise System Demo ===");
        
        // Initialize the system
        SplitWiseSystem splitWise = SplitWiseSystem.getInstance();
        splitWise.setUsers(new ArrayList<>());
        splitWise.setGroups(new ArrayList<>());
        
        // Create users
        System.out.println("\n--- Creating Users ---");
        User alice = splitWise.createUser("Alice", "alice@example.com");
        User bob = splitWise.createUser("Bob", "bob@example.com");
        User charlie = splitWise.createUser("Charlie", "charlie@example.com");
        User diana = splitWise.createUser("Diana", "diana@example.com");
        
        System.out.println("✅ Created users: Alice, Bob, Charlie, Diana");
        
        // Create groups
        System.out.println("\n--- Creating Groups ---");
        
        // Group 1: Trip to Goa
        List<User> tripMembers = Arrays.asList(alice, bob, charlie);
        Group tripGroup = splitWise.createGroup("Trip to Goa", tripMembers);
        
        // Group 2: Office Lunch
        List<User> lunchMembers = Arrays.asList(alice, bob, diana);
        Group lunchGroup = splitWise.createGroup("Office Lunch", lunchMembers);
        
        System.out.println("✅ Created groups: 'Trip to Goa' and 'Office Lunch'");
        
        // Test Scenario 1: Equal Split
        System.out.println("\n--- Test 1: Equal Split ---");
        System.out.println("🍕 Alice pays ₹1200 for dinner, split equally among Alice, Bob, Charlie");
        
        splitWise.addExpenseInGroup(tripGroup, 
            "Dinner at restaurant", 
            alice, 
            1200.0, 
            Arrays.asList(bob, charlie), 
            SplitMethod.EQUAL, 
            null);
        
        System.out.println("Balance after dinner:");
        tripGroup.getBalanceSheet().showBalance();
        
        // Test Scenario 2: Exact Split
        System.out.println("\n--- Test 2: Exact Split ---");
        System.out.println("🚗 Bob pays ₹900 for taxi, Alice owes ₹300, Charlie owes ₹200");
        
        Map<User, Double> exactDistribution = new HashMap<>();
        exactDistribution.put(alice, 300.0);
        exactDistribution.put(charlie, 200.0);

        splitWise.addExpenseInGroup(tripGroup, 
            "Taxi fare", 
            bob, 
            900.0, 
            Arrays.asList(alice, charlie), 
            SplitMethod.EXACT, 
            exactDistribution);
        
        System.out.println("Balance after taxi:");
        tripGroup.getBalanceSheet().showBalance();
        
        // Test Scenario 3: Percentage Split
        System.out.println("\n--- Test 3: Percentage Split ---");
        System.out.println("🏨 Charlie pays ₹3000 for hotel, Alice 40%, Bob 30%, Charlie 30%");
        
        Map<User, Double> percentageDistribution = new HashMap<>();
        percentageDistribution.put(alice, 40.0); // 40%
        percentageDistribution.put(bob, 30.0);   // 30%
        // Charlie pays, so he gets the remaining 30%

        splitWise.addExpenseInGroup(tripGroup, 
            "Hotel booking", 
            charlie, 
            3000.0, 
            Arrays.asList(alice, bob), 
            SplitMethod.PERCENTAGE, 
            percentageDistribution);
        
        System.out.println("Balance after hotel booking:");
        tripGroup.getBalanceSheet().showBalance();
        
        // Test Scenario 4: Different Group
        System.out.println("\n--- Test 4: Office Lunch Group ---");
        System.out.println("🍽️ Diana pays ₹600 for lunch, split equally among Alice, Bob, Diana");
        
        splitWise.addExpenseInGroup(lunchGroup, "Office lunch", 
            diana, 
            600.0, 
            Arrays.asList(alice, bob), 
            SplitMethod.EQUAL, 
            null);
        
        System.out.println("Lunch group balance:");
        lunchGroup.getBalanceSheet().showBalance();
        
        // Test Scenario 5: Individual User Balance
        System.out.println("\n--- Test 5: Individual User Balances ---");
        System.out.println("Alice's balance in Trip group:");
        tripGroup.getBalanceSheet().showUserBalance(alice);
        
        System.out.println("\nBob's balance in Trip group:");
        tripGroup.getBalanceSheet().showUserBalance(bob);
        
        // Test Scenario 6: Adding more expenses
        System.out.println("\n--- Test 6: More Expenses ---");
        System.out.println("🛒 Alice pays ₹450 for groceries, split equally in lunch group");
        
        splitWise.addExpenseInGroup(lunchGroup, 
            "Groceries", 
            alice, 
            450.0, 
            Arrays.asList(bob, diana), 
            SplitMethod.EQUAL, 
            null);
        
        System.out.println("Updated lunch group balance:");
        lunchGroup.getBalanceSheet().showBalance();
        
        // Summary
        System.out.println("\n=== Final Summary ===");
        System.out.println("Trip to Goa Group - Final Balances:");
        tripGroup.getBalanceSheet().showBalance();
        
        System.out.println("\nOffice Lunch Group - Final Balances:");
        lunchGroup.getBalanceSheet().showBalance();
        
        System.out.println("\n🎉 Demo completed successfully!");
        System.out.println("Total users: " + splitWise.getUsers().size());
        System.out.println("Total groups: " + splitWise.getGroups().size());
    }
}

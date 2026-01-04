package com.lld.lld.splitwise;

import java.util.*;
import java.util.concurrent.*;

import com.lld.lld.splitwise.enums.SplitMethod;
import com.lld.lld.splitwise.models.Group;
import com.lld.lld.splitwise.models.User;

public class SplitWiseConcurrencyDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println("=== SplitWise System Concurrency Demo ===");
        System.out.println("Demonstrating Runtime Exception when multiple users try to add expenses simultaneously\n");

        // Initialize SplitWise system
        SplitWiseSystem splitWise = SplitWiseSystem.getInstance();
        splitWise.setUsers(new ArrayList<>());
        splitWise.setGroups(new ArrayList<>());

        // Create users
        System.out.println("--- Creating Users ---");
        User alice = splitWise.createUser("Alice", "alice@example.com");
        User bob = splitWise.createUser("Bob", "bob@example.com");
        User charlie = splitWise.createUser("Charlie", "charlie@example.com");
        User david = splitWise.createUser("David", "david@example.com");

        // Create a group
        List<User> tripMembers = Arrays.asList(alice, bob, charlie, david);
        Group tripGroup = splitWise.createGroup("Trip to Goa", tripMembers);

        // Use more threads to increase chance of concurrent access
        ExecutorService executor = Executors.newFixedThreadPool(4);
        CountDownLatch startLatch = new CountDownLatch(1); // barrier to start all threads together
        List<CompletableFuture<String>> futures = new ArrayList<>();

        // Thread 1: Alice adding expense
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                startLatch.await(); // wait until all threads are ready
                System.out.println("[Thread 1] Alice attempting to add expense...");
                splitWise.addExpenseInGroup(
                    tripGroup,
                    "Dinner at restaurant",
                    alice,
                    1200.0,
                    Arrays.asList(bob, charlie, david),
                    SplitMethod.EQUAL,
                    null
                );
                return "✅ [Thread 1] Alice added expense successfully.";
            } catch (RuntimeException e) {
                System.out.println("❌ [Thread 1] Runtime Exception caught: " + e.getMessage());
                return "❌ [Thread 1] Runtime Exception: " + e.getMessage();
            } catch (Exception e) {
                System.out.println("❌ [Thread 1] Unexpected Exception: " + e.getMessage());
                return "❌ [Thread 1] Unexpected: " + e.getMessage();
            }
        }, executor);
        futures.add(future1);

        // Thread 2: Bob adding expense
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                startLatch.await();
                System.out.println("[Thread 2] Bob attempting to add expense...");
                Map<User, Double> exactDistribution = new HashMap<>();
                exactDistribution.put(alice, 300.0);
                exactDistribution.put(charlie, 200.0);
                exactDistribution.put(david, 400.0);

                splitWise.addExpenseInGroup(
                    tripGroup,
                    "Taxi fare",
                    bob,
                    900.0,
                    Arrays.asList(alice, charlie, david),
                    SplitMethod.EXACT,
                    exactDistribution
                );
                return "✅ [Thread 2] Bob added expense successfully.";
            } catch (RuntimeException e) {
                System.out.println("❌ [Thread 2] Runtime Exception caught: " + e.getMessage());
                return "❌ [Thread 2] Runtime Exception: " + e.getMessage();
            } catch (Exception e) {
                System.out.println("❌ [Thread 2] Unexpected Exception: " + e.getMessage());
                return "❌ [Thread 2] Unexpected: " + e.getMessage();
            }
        }, executor);
        futures.add(future2);

        // Thread 3: Charlie adding expense
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            try {
                startLatch.await();
                System.out.println("[Thread 3] Charlie attempting to add expense...");
                splitWise.addExpenseInGroup(
                    tripGroup,
                    "Hotel booking",
                    charlie,
                    2000.0,
                    Arrays.asList(alice, bob, david),
                    SplitMethod.EQUAL,
                    null
                );
                return "✅ [Thread 3] Charlie added expense successfully.";
            } catch (RuntimeException e) {
                System.out.println("❌ [Thread 3] Runtime Exception caught: " + e.getMessage());
                return "❌ [Thread 3] Runtime Exception: " + e.getMessage();
            } catch (Exception e) {
                System.out.println("❌ [Thread 3] Unexpected Exception: " + e.getMessage());
                return "❌ [Thread 3] Unexpected: " + e.getMessage();
            }
        }, executor);
        futures.add(future3);

        // Thread 4: David adding expense
        CompletableFuture<String> future4 = CompletableFuture.supplyAsync(() -> {
            try {
                startLatch.await();
                System.out.println("[Thread 4] David attempting to add expense...");
                splitWise.addExpenseInGroup(
                    tripGroup,
                    "Shopping",
                    david,
                    1500.0,
                    Arrays.asList(alice, bob, charlie),
                    SplitMethod.EQUAL,
                    null
                );
                return "✅ [Thread 4] David added expense successfully.";
            } catch (RuntimeException e) {
                System.out.println("❌ [Thread 4] Runtime Exception caught: " + e.getMessage());
                return "❌ [Thread 4] Runtime Exception: " + e.getMessage();
            } catch (Exception e) {
                System.out.println("❌ [Thread 4] Unexpected Exception: " + e.getMessage());
                return "❌ [Thread 4] Unexpected: " + e.getMessage();
            }
        }, executor);
        futures.add(future4);

        // Release latch so all threads start together
        System.out.println("\n🚦 Starting all 4 threads simultaneously to trigger concurrent access...\n");
        Thread.sleep(100); // small delay to ensure all threads are waiting
        startLatch.countDown();

        // Wait for all tasks
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        System.out.println("\n=== Final Results ===");
        for (CompletableFuture<String> future : futures) {
            System.out.println(future.get());
        }

        System.out.println("\n=== Final Balances ===");
        tripGroup.getBalanceSheet().showBalance();

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }
}

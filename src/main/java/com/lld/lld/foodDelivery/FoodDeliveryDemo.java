package com.lld.lld.foodDelivery;

import java.util.Arrays;
import java.util.List;

import com.lld.lld.foodDelivery.facade.FoodDeliverySystem;
import com.lld.lld.foodDelivery.models.Agent;
import com.lld.lld.foodDelivery.models.Dish;
import com.lld.lld.foodDelivery.models.Menu;
import com.lld.lld.foodDelivery.models.Order;
import com.lld.lld.foodDelivery.models.Resturent;
import com.lld.lld.foodDelivery.models.User;
import com.lld.lld.foodDelivery.strategy.payment.UPIPayment;

/**
 * Demonstration of the Food Delivery System
 * Shows usage of Facade Pattern, Strategy Pattern (Search, Payment), and
 * Observer Pattern (Order notifications)
 */
public class FoodDeliveryDemo {
    public static void main(String[] args) {
        // Get the Food Delivery System instance (Singleton Facade)
        FoodDeliverySystem foodSystem = FoodDeliverySystem.getInstance();

        System.out.println("=".repeat(60));
        System.out.println("FOOD DELIVERY SYSTEM DEMO");
        System.out.println("=".repeat(60));

        // ======================== SETUP: Register Users ========================
        System.out.println("\n--- Registering Users ---");
        User user1 = new User("U001", "Rahul", "123 MG Road, Bangalore");
        User user2 = new User("U002", "Priya", "456 Brigade Road, Bangalore");

        foodSystem.registerUser(user1);
        foodSystem.registerUser(user2);

        // ======================== SETUP: Register Restaurants ========================
        System.out.println("\n--- Registering Restaurants ---");
        Resturent restaurant1 = new Resturent("R001", "Pizza Palace", "HSR Layout, Bangalore");
        Resturent restaurant2 = new Resturent("R002", "Biryani House", "Koramangala, Bangalore");

        foodSystem.registerResturent(restaurant1);
        foodSystem.registerResturent(restaurant2);

        // ======================== SETUP: Add Menus ========================
        System.out.println("\n--- Adding Menus ---");
        Menu pizzaMenu = new Menu("M001", "Pizza Menu");
        pizzaMenu.addDish(new Dish("D001", "Margherita Pizza", 299.0));
        pizzaMenu.addDish(new Dish("D002", "Pepperoni Pizza", 399.0));
        pizzaMenu.addDish(new Dish("D003", "Farmhouse Pizza", 449.0));

        Menu biryaniMenu = new Menu("M002", "Biryani Menu");
        biryaniMenu.addDish(new Dish("D004", "Chicken Biryani", 249.0));
        biryaniMenu.addDish(new Dish("D005", "Mutton Biryani", 349.0));

        restaurant1.addMenu(pizzaMenu);
        restaurant2.addMenu(biryaniMenu);

        foodSystem.addMenu(pizzaMenu);
        foodSystem.addMenu(biryaniMenu);

        // ======================== SETUP: Register Agents ========================
        System.out.println("\n--- Registering Delivery Agents ---");
        Agent agent1 = new Agent("A001", "Suresh", "Indiranagar, Bangalore");
        Agent agent2 = new Agent("A002", "Ramesh", "Whitefield, Bangalore");

        foodSystem.registerAgent(agent1);
        foodSystem.registerAgent(agent2);

        // ======================== DEMO: Search Functionality ========================
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO: SEARCH FUNCTIONALITY (Strategy Pattern)");
        System.out.println("=".repeat(60));

        System.out.println("\n--- Searching for 'Pizza' restaurants ---");
        List<Resturent> searchResults = foodSystem.browseResturents("Pizza");
        for (Resturent r : searchResults) {
            System.out.println("  Found: " + r.getName());
        }

        System.out.println("\n--- Searching for 'Biryani' menus ---");
        List<Menu> menuResults = foodSystem.browseMenus("Biryani");
        for (Menu m : menuResults) {
            System.out.println("  Found: " + m.getTitle());
        }

        // ======================== DEMO: Order Lifecycle with Observer
        // ========================
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO: ORDER LIFECYCLE (Observer Pattern)");
        System.out.println("=".repeat(60));

        // Create order with dishes
        System.out.println("\n--- Placing Order ---");
        List<Dish> orderDishes = Arrays.asList(
                new Dish("D001", "Margherita Pizza", 299.0, 2),
                new Dish("D002", "Pepperoni Pizza", 399.0, 1));
        Order order = new Order("ORD001", "U001", orderDishes);

        // Place order (User and Restaurant become observers)
        foodSystem.placeOrder(order, "R001");

        System.out.println("\n--- Confirming Order ---");
        foodSystem.confirmOrder("ORD001");

        System.out.println("\n--- Preparing Order ---");
        foodSystem.prepareOrder("ORD001");

        System.out.println("\n--- Order Ready for Pickup ---");
        foodSystem.orderReadyForPickup("ORD001");

        System.out.println("\n--- Assigning Delivery Agent ---");
        foodSystem.assignAgent("ORD001");

        System.out.println("\n--- Starting Delivery ---");
        foodSystem.startDelivery("ORD001");

        System.out.println("\n--- Completing Delivery ---");
        foodSystem.completeDelivery("ORD001");

        // ======================== DEMO: Payment ========================
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO: PAYMENT (Strategy Pattern)");
        System.out.println("=".repeat(60));

        System.out.println("\n--- Processing UPI Payment ---");
        foodSystem.setPaymentStrategy(new UPIPayment("rahul@upi"));
        foodSystem.processPayment(order.getPrice());

        // ======================== DEMO: Track Order ========================
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO: TRACK ORDER");
        System.out.println("=".repeat(60));

        System.out.println("\n--- Tracking Order ---");
        foodSystem.trackOrder("ORD001");

        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO COMPLETE");
        System.out.println("=".repeat(60));
    }
}

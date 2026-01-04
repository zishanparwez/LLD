package com.lld.lld.foodDelivery.facade;

import java.util.List;

import com.lld.lld.foodDelivery.models.Agent;
import com.lld.lld.foodDelivery.models.Menu;
import com.lld.lld.foodDelivery.models.Order;
import com.lld.lld.foodDelivery.models.Resturent;
import com.lld.lld.foodDelivery.models.User;
import com.lld.lld.foodDelivery.repository.AgentRepository;
import com.lld.lld.foodDelivery.repository.MenuRepository;
import com.lld.lld.foodDelivery.repository.ResturentRepository;
import com.lld.lld.foodDelivery.repository.UserRepository;
import com.lld.lld.foodDelivery.service.OrderService;
import com.lld.lld.foodDelivery.service.PaymentService;
import com.lld.lld.foodDelivery.service.SearchService;
import com.lld.lld.foodDelivery.strategy.payment.PaymentStrategy;
import com.lld.lld.foodDelivery.strategy.search.SearchByMenu;
import com.lld.lld.foodDelivery.strategy.search.SearchByResturent;

/**
 * Facade Pattern - Provides a simplified interface to the Food Delivery
 * subsystem
 */
public class FoodDeliverySystem {
    private OrderService orderService;
    private SearchService<Resturent> resturentSearchService;
    private SearchService<Menu> menuSearchService;
    private PaymentService paymentService;

    // Repositories
    private UserRepository userRepository;
    private ResturentRepository resturentRepository;
    private AgentRepository agentRepository;
    private MenuRepository menuRepository;

    private static FoodDeliverySystem instance;

    private FoodDeliverySystem() {
        this.orderService = new OrderService();
        this.resturentSearchService = new SearchService<>(new SearchByResturent());
        this.menuSearchService = new SearchService<>(new SearchByMenu());
        this.paymentService = new PaymentService();
        this.userRepository = UserRepository.getInstance();
        this.resturentRepository = ResturentRepository.getInstance();
        this.agentRepository = AgentRepository.getInstance();
        this.menuRepository = MenuRepository.getInstance();
    }

    public static synchronized FoodDeliverySystem getInstance() {
        if (instance == null) {
            instance = new FoodDeliverySystem();
        }
        return instance;
    }

    // ======================== Registration Methods ========================

    public void registerUser(User user) {
        userRepository.registerUser(user);
        System.out.println("Registered user: " + user.getName());
    }

    public void registerResturent(Resturent resturent) {
        resturentRepository.registerResturent(resturent);
        System.out.println("Registered restaurant: " + resturent.getName());
    }

    public void registerAgent(Agent agent) {
        agentRepository.registerAgent(agent);
        System.out.println("Registered agent: " + agent.getName());
    }

    public void addMenu(Menu menu) {
        menuRepository.addMenu(menu);
        System.out.println("Added menu: " + menu.getTitle());
    }

    // ======================== Search Methods ========================

    public List<Resturent> browseResturents(String query) {
        return resturentSearchService.search(query);
    }

    public List<Menu> browseMenus(String query) {
        return menuSearchService.search(query);
    }

    // ======================== Order Methods ========================

    public Order placeOrder(Order order, String resturentId) {
        return orderService.placeOrder(order, resturentId);
    }

    public void confirmOrder(String orderId) {
        orderService.confirmOrder(orderId);
    }

    public void prepareOrder(String orderId) {
        orderService.prepareOrder(orderId);
    }

    public void orderReadyForPickup(String orderId) {
        orderService.orderReadyForPickup(orderId);
    }

    public void assignAgent(String orderId) {
        orderService.assignAgent(orderId);
    }

    public void startDelivery(String orderId) {
        orderService.startDelivery(orderId);
    }

    public void completeDelivery(String orderId) {
        orderService.completeDelivery(orderId);
    }

    public void cancelOrder(String orderId) {
        orderService.cancelOrder(orderId);
        System.out.println("Order " + orderId + " cancelled");
    }

    public Order trackOrder(String orderId) {
        Order order = orderService.getOrder(orderId);
        if (order != null) {
            System.out.println("Order Status: " + order.getOrderstatus());
        } else {
            System.out.println("Order not found: " + orderId);
        }
        return order;
    }

    // ======================== Payment Methods ========================

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        paymentService.setPaymentStrategy(paymentStrategy);
    }

    public boolean processPayment(double amount) {
        return paymentService.processPayment(amount);
    }

    // ======================== Getter Methods ========================

    public User getUser(String userId) {
        return userRepository.getUser(userId);
    }

    public Resturent getResturent(String resturentId) {
        return resturentRepository.getResturent(resturentId);
    }

    public Agent getAgent(String agentId) {
        return agentRepository.getAgent(agentId);
    }
}

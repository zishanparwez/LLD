package com.lld.lld.foodDelivery.service;

import java.util.List;

import com.lld.lld.foodDelivery.enums.OrderStatus;
import com.lld.lld.foodDelivery.models.Agent;
import com.lld.lld.foodDelivery.models.Order;
import com.lld.lld.foodDelivery.models.Resturent;
import com.lld.lld.foodDelivery.models.User;
import com.lld.lld.foodDelivery.repository.AgentRepository;
import com.lld.lld.foodDelivery.repository.OrderRepository;
import com.lld.lld.foodDelivery.repository.ResturentRepository;
import com.lld.lld.foodDelivery.repository.UserRepository;

public class OrderService {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private ResturentRepository resturentRepository;
    private AgentRepository agentRepository;

    public OrderService() {
        this.orderRepository = OrderRepository.getInstance();
        this.userRepository = UserRepository.getInstance();
        this.resturentRepository = ResturentRepository.getInstance();
        this.agentRepository = AgentRepository.getInstance();
    }

    public Order placeOrder(Order order, String resturentId) {
        // Get user and restaurant
        User user = userRepository.getUser(order.getUserId());
        Resturent resturent = resturentRepository.getResturent(resturentId);

        if (user == null || resturent == null) {
            System.out.println("Invalid user or restaurant!");
            return null;
        }

        // Add observers (User and Restaurant will receive updates)
        order.addObserver(user);
        order.addObserver(resturent);

        // Save order and set restaurant
        orderRepository.createOrder(order);
        order.setResturentId(resturentId);

        // Notify observers about order creation
        order.updateStatus(OrderStatus.PENDING);

        return order;
    }

    public void confirmOrder(String orderId) {
        Order order = orderRepository.getOrder(orderId);
        if (order != null) {
            order.updateStatus(OrderStatus.CONFIRMED);
        }
    }

    public void prepareOrder(String orderId) {
        Order order = orderRepository.getOrder(orderId);
        if (order != null) {
            order.updateStatus(OrderStatus.PREPARING);
        }
    }

    public void orderReadyForPickup(String orderId) {
        Order order = orderRepository.getOrder(orderId);
        if (order != null) {
            order.updateStatus(OrderStatus.READY_FOR_PICKUP);
        }
    }

    public void assignAgent(String orderId) {
        Order order = orderRepository.getOrder(orderId);
        if (order == null) {
            System.out.println("Order not found!");
            return;
        }

        Agent agent = agentRepository.findAvailableAgent();
        if (agent == null) {
            System.out.println("No available agents!");
            return;
        }

        // Assign agent and add as observer
        order.setAgentId(agent.getAgentId());
        order.addObserver(agent);
        agent.setAvailable(false);

        System.out.println("Agent " + agent.getName() + " assigned to order " + orderId);
    }

    public void startDelivery(String orderId) {
        Order order = orderRepository.getOrder(orderId);
        if (order != null) {
            order.updateStatus(OrderStatus.OUT_FOR_DELIVERY);
        }
    }

    public void completeDelivery(String orderId) {
        Order order = orderRepository.getOrder(orderId);
        if (order != null) {
            order.updateStatus(OrderStatus.DELIVERED);
        }
    }

    public void cancelOrder(String orderId) {
        orderRepository.cancelOrder(orderId);
    }

    public Order getOrder(String orderId) {
        return orderRepository.getOrder(orderId);
    }
}

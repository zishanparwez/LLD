package com.lld.lld.foodDelivery.repository;

import java.util.HashMap;
import java.util.Map;

import com.lld.lld.foodDelivery.enums.OrderStatus;
import com.lld.lld.foodDelivery.models.Order;

public class OrderRepository {

    private static OrderRepository instance;
    private Map<String, Order> orders;

    private OrderRepository() {
        this.orders = new HashMap<>();
    }

    public static synchronized OrderRepository getInstance() {
        if (instance == null) {
            instance = new OrderRepository();
        }
        return instance;
    }

    public void createOrder(Order order) {
        orders.put(order.getOrderId(), order);
    }

    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }

    public void placeOrderToResturent(String resturentId, Order order) {
        Order currentOrder = orders.get(order.getOrderId());
        if (currentOrder != null) {
            currentOrder.setResturentId(resturentId);
            currentOrder.updateStatus(OrderStatus.CONFIRMED);
        }
    }

    public void updateOrderStatus(String orderId, OrderStatus status) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.updateStatus(status);
        }
    }

    public void cancelOrder(String orderId) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.updateStatus(OrderStatus.CANCELLED);
        }
    }
}

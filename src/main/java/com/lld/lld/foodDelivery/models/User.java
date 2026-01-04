package com.lld.lld.foodDelivery.models;

import com.lld.lld.foodDelivery.observer.OrderObserver;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User implements OrderObserver {

    private String userId;
    private String name;
    private String address;

    public User(String userId, String name, String address) {
        this.userId = userId;
        this.name = name;
        this.address = address;
    }

    @Override
    public void update(Order order) {
        switch (order.getOrderstatus()) {
            case PENDING:
                System.out.println("[" + name + "] Your order " + order.getOrderId() + " is created!");
                break;
            case CONFIRMED:
                System.out.println("[" + name + "] Your order " + order.getOrderId() + " is confirmed!");
                break;
            case PREPARING:
                System.out.println("[" + name + "] Your order " + order.getOrderId() + " is being prepared!");
                break;
            case READY_FOR_PICKUP:
                System.out.println("[" + name + "] Your order " + order.getOrderId() + " is ready for pickup!");
                break;
            case OUT_FOR_DELIVERY:
                System.out.println("[" + name + "] Your order " + order.getOrderId() + " is out for delivery!");
                break;
            case DELIVERED:
                System.out.println("[" + name + "] Your order " + order.getOrderId() + " has been delivered!");
                break;
            case CANCELLED:
                System.out.println("[" + name + "] Your order " + order.getOrderId() + " is cancelled!");
                break;
            default:
                break;
        }
    }

    @Override
    public String toString() {
        return "User{userId='" + userId + "', name='" + name + "'}";
    }
}

package com.lld.lld.foodDelivery.models;

import com.lld.lld.foodDelivery.observer.OrderObserver;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Agent implements OrderObserver {
    private String agentId;
    private String name;
    private String address;
    private boolean available;

    public Agent(String agentId, String name, String address) {
        this.agentId = agentId;
        this.name = name;
        this.address = address;
        this.available = true;
    }

    @Override
    public void update(Order order) {
        switch (order.getOrderstatus()) {
            case READY_FOR_PICKUP:
                System.out.println("[Agent " + name + "] Order " + order.getOrderId() + " is ready for pickup!");
                break;
            case OUT_FOR_DELIVERY:
                System.out.println("[Agent " + name + "] Delivering order " + order.getOrderId());
                break;
            case DELIVERED:
                System.out.println("[Agent " + name + "] Order " + order.getOrderId() + " delivered successfully!");
                this.available = true; // Agent becomes available again
                break;
            case CANCELLED:
                System.out.println("[Agent " + name + "] Order " + order.getOrderId() + " was cancelled!");
                this.available = true;
                break;
            default:
                break;
        }
    }

    @Override
    public String toString() {
        return "Agent{agentId='" + agentId + "', name='" + name + "', available=" + available + "}";
    }
}

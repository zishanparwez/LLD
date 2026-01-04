package com.lld.lld.foodDelivery.models;

import java.util.ArrayList;
import java.util.List;

import com.lld.lld.foodDelivery.observer.OrderObserver;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Resturent implements OrderObserver {
    private String resturentId;
    private String name;
    private String address;
    private List<Menu> menus;

    public Resturent(String resturentId, String name, String address) {
        this.resturentId = resturentId;
        this.name = name;
        this.address = address;
        this.menus = new ArrayList<>();
    }

    public void addMenu(Menu menu) {
        menus.add(menu);
    }

    public void removeMenu(Menu menu) {
        menus.remove(menu);
    }

    @Override
    public void update(Order order) {
        switch (order.getOrderstatus()) {
            case CONFIRMED:
                System.out.println("[Restaurant " + name + "] New order received: " + order.getOrderId());
                break;
            case PREPARING:
                System.out.println("[Restaurant " + name + "] Preparing order: " + order.getOrderId());
                break;
            case READY_FOR_PICKUP:
                System.out.println("[Restaurant " + name + "] Order " + order.getOrderId() + " is ready for pickup!");
                break;
            case CANCELLED:
                System.out.println("[Restaurant " + name + "] Order " + order.getOrderId() + " was cancelled!");
                break;
            default:
                break;
        }
    }

    @Override
    public String toString() {
        return "Resturent{resturentId='" + resturentId + "', name='" + name + "'}";
    }
}

package com.lld.lld.foodDelivery.observer;

import java.util.ArrayList;
import java.util.List;

import com.lld.lld.foodDelivery.models.Order;

public abstract class OrderSubject {
    private final List<OrderObserver> observers = new ArrayList<>();

    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (OrderObserver observer: observers) {
            observer.update((Order) this);
        }
    }
}

package com.lld.lld.foodDelivery.observer;

import com.lld.lld.foodDelivery.models.Order;

public interface OrderObserver {
    void update(Order order);
}

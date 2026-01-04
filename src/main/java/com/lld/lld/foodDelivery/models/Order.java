package com.lld.lld.foodDelivery.models;

import java.util.List;

import com.lld.lld.foodDelivery.enums.OrderStatus;
import com.lld.lld.foodDelivery.observer.OrderSubject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order extends OrderSubject {
    private String orderId;
    private String userId;
    private String agentId;
    private String resturentId;
    private Double price;
    private OrderStatus orderstatus;
    private List<Dish> dishes;

    public Order(String orderId, String userId, List<Dish> dishes) {
        this.orderId = orderId;
        this.userId = userId;
        this.dishes = dishes;
        this.orderstatus = OrderStatus.PENDING;
        this.price = calculateTotalPrice();
    }

    private Double calculateTotalPrice() {
        if (dishes == null || dishes.isEmpty()) {
            return 0.0;
        }
        return dishes.stream()
                .mapToDouble(dish -> dish.getPrice() * dish.getQuantity())
                .sum();
    }

    public void updateStatus(OrderStatus newStatus) {
        this.orderstatus = newStatus;
        notifyObservers(); // Notify all observers about status change
    }

    @Override
    public String toString() {
        return "Order{orderId='" + orderId + "', status=" + orderstatus + ", price=" + price + "}";
    }
}

package com.lld.lld.foodDelivery.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Dish {
    private String dishId;
    private String name;
    private Double price;
    private Integer quantity;
    private boolean available;

    public Dish(String dishId, String name, Double price) {
        this.dishId = dishId;
        this.name = name;
        this.price = price;
        this.quantity = 1;
        this.available = true;
    }

    public Dish(String dishId, String name, Double price, Integer quantity) {
        this.dishId = dishId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.available = true;
    }

    @Override
    public String toString() {
        return "Dish{name='" + name + "', price=" + price + ", quantity=" + quantity + "}";
    }
}

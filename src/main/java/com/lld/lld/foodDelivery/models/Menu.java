package com.lld.lld.foodDelivery.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Menu {
    private String menuId;
    private String title;
    private List<Dish> dishes;

    public Menu(String menuId, String title) {
        this.menuId = menuId;
        this.title = title;
        this.dishes = new ArrayList<>();
    }

    public void addDish(Dish dish) {
        dishes.add(dish);
    }

    public void removeDish(Dish dish) {
        dishes.remove(dish);
    }

    @Override
    public String toString() {
        return "Menu{menuId='" + menuId + "', title='" + title + "', dishCount=" + dishes.size() + "}";
    }
}

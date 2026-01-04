package com.lld.lld.foodDelivery.strategy.search;

import java.util.List;

import com.lld.lld.foodDelivery.models.Menu;
import com.lld.lld.foodDelivery.repository.MenuRepository;

public class SearchByMenu implements SearchStrategy<Menu> {

    private MenuRepository menuRepository;

    public SearchByMenu() {
        this.menuRepository = MenuRepository.getInstance();
    }

    @Override
    public List<Menu> search(String query) {
        System.out.println("Searching menus by title: " + query);
        List<Menu> results = this.menuRepository.searchMenu(query);
        System.out.println("Found " + results.size() + " menus");
        return results;
    }
}

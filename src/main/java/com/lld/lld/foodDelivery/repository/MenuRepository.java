package com.lld.lld.foodDelivery.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.lld.lld.foodDelivery.models.Menu;

public class MenuRepository {

    private static MenuRepository menuInstance;
    private Map<String, Menu> menus;

    private MenuRepository() {
        this.menus = new HashMap<>();
    }

    public static synchronized MenuRepository getInstance() {
        if (menuInstance == null) {
            menuInstance = new MenuRepository();
        }
        return menuInstance;
    }

    public void addMenu(Menu menu) {
        menus.put(menu.getMenuId(), menu);
    }

    public Menu getMenu(String menuId) {
        return menus.get(menuId);
    }

    public void removeMenu(String menuId) {
        menus.remove(menuId);
    }

    public List<Menu> searchMenu(String query) {
        String searchTerm = query.toLowerCase();
        return menus.values().stream()
                .filter(menu -> menu.getTitle().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }

    public List<Menu> getAllMenus() {
        return menus.values().stream().collect(Collectors.toList());
    }
}

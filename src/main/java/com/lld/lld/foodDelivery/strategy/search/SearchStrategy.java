package com.lld.lld.foodDelivery.strategy.search;

import java.util.List;

/**
 * Strategy interface for searching
 * Uses generics to allow different return types (Resturent, Menu, etc.)
 */
public interface SearchStrategy<T> {
    List<T> search(String query);
}

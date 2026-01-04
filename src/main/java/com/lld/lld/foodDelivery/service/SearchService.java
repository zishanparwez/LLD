package com.lld.lld.foodDelivery.service;

import java.util.List;

import com.lld.lld.foodDelivery.strategy.search.SearchStrategy;

/**
 * Search service using Strategy pattern
 * 
 * @param <T> the type of items being searched (Resturent, Menu, etc.)
 */
public class SearchService<T> {

    private SearchStrategy<T> searchStrategy;

    public SearchService() {
    }

    public SearchService(SearchStrategy<T> searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public void setSearchStrategy(SearchStrategy<T> searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public List<T> search(String query) {
        if (searchStrategy == null) {
            throw new IllegalStateException("Search strategy not set!");
        }
        return searchStrategy.search(query);
    }
}

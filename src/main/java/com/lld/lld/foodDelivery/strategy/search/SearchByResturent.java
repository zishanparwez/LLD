package com.lld.lld.foodDelivery.strategy.search;

import java.util.List;

import com.lld.lld.foodDelivery.models.Resturent;
import com.lld.lld.foodDelivery.repository.ResturentRepository;

public class SearchByResturent implements SearchStrategy<Resturent> {
    private ResturentRepository resturentRepository;

    public SearchByResturent() {
        this.resturentRepository = ResturentRepository.getInstance();
    }

    @Override
    public List<Resturent> search(String query) {
        System.out.println("Searching restaurants by name: " + query);
        List<Resturent> results = resturentRepository.searchResturent(query);
        System.out.println("Found " + results.size() + " restaurants");
        return results;
    }
}

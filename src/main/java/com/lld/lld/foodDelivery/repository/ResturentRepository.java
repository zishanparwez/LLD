package com.lld.lld.foodDelivery.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.lld.lld.foodDelivery.models.Resturent;

public class ResturentRepository {
    private static ResturentRepository instance;
    private Map<String, Resturent> resturents;

    private ResturentRepository() {
        this.resturents = new HashMap<>();
    }

    public static synchronized ResturentRepository getInstance() {
        if (instance == null) {
            instance = new ResturentRepository();
        }
        return instance;
    }

    public void registerResturent(Resturent resturent) {
        resturents.put(resturent.getResturentId(), resturent);
    }

    public Resturent getResturent(String resturentId) {
        return resturents.get(resturentId);
    }

    public List<Resturent> searchResturent(String query) {
        String searchTerm = query.toLowerCase();
        return resturents.values().stream()
                .filter(resturent -> resturent.getName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }

    public List<Resturent> getAllResturents() {
        return resturents.values().stream().collect(Collectors.toList());
    }
}

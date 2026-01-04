package com.lld.lld.foodDelivery.repository;

import java.util.HashMap;
import java.util.Map;

import com.lld.lld.foodDelivery.models.User;

public class UserRepository {
    private static UserRepository instance;
    private Map<String, User> users;

    private UserRepository() {
        this.users = new HashMap<>();
    }

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public void registerUser(User user) {
        users.put(user.getUserId(), user);
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public boolean userExists(String userId) {
        return users.containsKey(userId);
    }
}

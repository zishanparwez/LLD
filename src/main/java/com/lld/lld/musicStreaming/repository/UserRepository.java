package com.lld.lld.musicStreaming.repository;

import java.util.HashMap;
import java.util.Map;

import com.lld.lld.musicStreaming.models.User;

/**
 * In-memory repository for storing and retrieving users
 * Singleton pattern for centralized user storage
 */
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

    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public boolean userExists(String userId) {
        return users.containsKey(userId);
    }

    public void clear() {
        users.clear();
    }
}

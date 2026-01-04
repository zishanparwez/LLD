package com.lld.lld.musicStreaming.recomendation;

import java.util.ArrayList;
import java.util.List;

import com.lld.lld.musicStreaming.models.Song;
import com.lld.lld.musicStreaming.models.User;
import com.lld.lld.musicStreaming.repository.UserRepository;

/**
 * Service for getting song recommendations
 * Uses Strategy pattern - allows switching between different recommendation
 * strategies
 */
public class RecomendataionService {
    private RecomendationStrategy recomendationStrategy;
    private UserRepository userRepository;

    public RecomendataionService() {
        // Default to recommendation by listening history
        this.recomendationStrategy = new RecomendByListingHistory();
        this.userRepository = UserRepository.getInstance();
    }

    public RecomendataionService(RecomendationStrategy recomendationStrategy) {
        this.recomendationStrategy = recomendationStrategy;
        this.userRepository = UserRepository.getInstance();
    }

    /**
     * Set the recommendation strategy at runtime
     * 
     * @param recomendationStrategy the strategy to use for recommendations
     */
    public void setRecomendationStrategy(RecomendationStrategy recomendationStrategy) {
        this.recomendationStrategy = recomendationStrategy;
    }

    /**
     * Get recommendations for a user by userId
     * 
     * @param userId the ID of the user
     * @return list of recommended songs
     */
    public List<Song> getRecomendation(String userId) {
        if (recomendationStrategy == null) {
            throw new IllegalStateException("Recommendation strategy not set!");
        }

        User user = userRepository.getUser(userId);
        if (user == null) {
            System.out.println("User not found: " + userId);
            return new ArrayList<>();
        }

        return recomendationStrategy.getRecomendation(user);
    }
}

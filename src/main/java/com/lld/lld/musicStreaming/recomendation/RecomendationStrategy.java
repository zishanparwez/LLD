package com.lld.lld.musicStreaming.recomendation;

import java.util.List;

import com.lld.lld.musicStreaming.models.Song;
import com.lld.lld.musicStreaming.models.User;

/**
 * Strategy interface for song recommendations
 * Implements Strategy pattern - allows different recommendation algorithms
 */
public interface RecomendationStrategy {
    /**
     * Get song recommendations for a user
     * 
     * @param user the user to get recommendations for
     * @return list of recommended songs
     */
    List<Song> getRecomendation(User user);
}

package com.lld.lld.musicStreaming.search;

import java.util.List;

import com.lld.lld.musicStreaming.models.Song;

/**
 * Strategy interface for searching songs
 * Implements Strategy pattern - allows different search algorithms
 */
public interface SearchStrategy {
    /**
     * Search for songs based on the query
     * 
     * @param query the search term
     * @return list of songs matching the query
     */
    List<Song> search(String query);
}

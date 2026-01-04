package com.lld.lld.musicStreaming.search;

import java.util.List;

import com.lld.lld.musicStreaming.models.Song;

/**
 * Service for searching songs
 * Uses Strategy pattern - allows switching between different search strategies
 */
public class SearchService {
    private SearchStrategy searchStrategy;

    public SearchService() {
        // Default to search by title
        this.searchStrategy = new SearchByTitle();
    }

    public SearchService(SearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    /**
     * Set the search strategy at runtime
     * 
     * @param searchStrategy the strategy to use for searching
     */
    public void setSearchStrategy(SearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    /**
     * Search for songs using the current strategy
     * 
     * @param query the search term
     * @return list of songs matching the query
     */
    public List<Song> search(String query) {
        if (searchStrategy == null) {
            throw new IllegalStateException("Search strategy not set!");
        }
        return searchStrategy.search(query);
    }
}

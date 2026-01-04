package com.lld.lld.musicStreaming.search;

import java.util.List;

import com.lld.lld.musicStreaming.models.Song;
import com.lld.lld.musicStreaming.repository.SongRepository;

/**
 * Search strategy that searches songs by title
 * Uses case-insensitive partial matching
 */
public class SearchByTitle implements SearchStrategy {

    private SongRepository songRepository;

    public SearchByTitle() {
        this.songRepository = SongRepository.getInstance();
    }

    @Override
    public List<Song> search(String query) {
        System.out.println("Searching songs by title: " + query);
        List<Song> results = songRepository.searchByTitle(query);
        System.out.println("Found " + results.size() + " songs matching title '" + query + "'");
        return results;
    }
}

package com.lld.lld.musicStreaming.search;

import java.util.List;

import com.lld.lld.musicStreaming.models.Song;
import com.lld.lld.musicStreaming.repository.SongRepository;

/**
 * Search strategy that searches songs by artist name/ID
 */
public class SearchByArtist implements SearchStrategy {

    private SongRepository songRepository;

    public SearchByArtist() {
        this.songRepository = SongRepository.getInstance();
    }

    @Override
    public List<Song> search(String artistId) {
        System.out.println("Searching songs by artist ID: " + artistId);
        List<Song> results = songRepository.searchByArtistId(artistId);
        System.out.println("Found " + results.size() + " songs by artist '" + artistId + "'");
        return results;
    }
}

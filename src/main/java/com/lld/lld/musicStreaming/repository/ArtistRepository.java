package com.lld.lld.musicStreaming.repository;

import java.util.HashMap;
import java.util.Map;

import com.lld.lld.musicStreaming.models.Artist;

/**
 * In-memory repository for storing and retrieving artists
 * Singleton pattern for centralized artist storage
 */
public class ArtistRepository {
    private static ArtistRepository instance;
    private Map<String, Artist> artists;

    private ArtistRepository() {
        this.artists = new HashMap<>();
    }

    public static synchronized ArtistRepository getInstance() {
        if (instance == null) {
            instance = new ArtistRepository();
        }
        return instance;
    }

    public void addArtist(Artist artist) {
        artists.put(artist.getArtistId(), artist);
    }

    public Artist getArtist(String artistId) {
        return artists.get(artistId);
    }

    public boolean artistExists(String artistId) {
        return artists.containsKey(artistId);
    }

    public void clear() {
        artists.clear();
    }
}

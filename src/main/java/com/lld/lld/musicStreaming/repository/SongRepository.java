package com.lld.lld.musicStreaming.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.lld.lld.musicStreaming.models.Song;

/**
 * In-memory repository for storing and retrieving songs
 * Singleton pattern for centralized song storage
 */
public class SongRepository {
    private static SongRepository instance;
    private Map<String, Song> songs;

    private SongRepository() {
        this.songs = new HashMap<>();
    }

    public static synchronized SongRepository getInstance() {
        if (instance == null) {
            instance = new SongRepository();
        }
        return instance;
    }

    public void addSong(Song song) {
        songs.put(song.getSongId(), song);
    }

    public Song getSong(String songId) {
        return songs.get(songId);
    }

    public List<Song> getAllSongs() {
        return songs.values().stream().collect(Collectors.toList());
    }

    public List<Song> searchByTitle(String title) {
        String searchTerm = title.toLowerCase();
        return songs.values().stream()
                .filter(song -> song.getTitle().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }

    public List<Song> searchByArtistId(String artistId) {
        return songs.values().stream()
                .filter(song -> song.getArtistId().equals(artistId))
                .collect(Collectors.toList());
    }

    public void clear() {
        songs.clear();
    }
}

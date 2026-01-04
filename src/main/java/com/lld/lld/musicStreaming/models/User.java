package com.lld.lld.musicStreaming.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String userId;
    private String name;
    private String email;
    private List<PlayList> playLists;
    private List<Artist> followedArtists;
    private List<Song> listeningHistory;

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.playLists = new ArrayList<>();
        this.followedArtists = new ArrayList<>();
        this.listeningHistory = new ArrayList<>();
    }

    public void addToListeningHistory(Song song) {
        listeningHistory.add(song);
    }

    public void addPlaylist(PlayList playlist) {
        playLists.add(playlist);
    }

    public void removePlayList(PlayList playlist) {
        playLists.remove(playlist);
    }

    public void followArtist(Artist artist) {
        followedArtists.add(artist);
    }

    public void unfollowArtist(Artist artist) {
        followedArtists.remove(artist);
    }
}

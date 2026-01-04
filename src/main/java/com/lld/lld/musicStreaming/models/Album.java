package com.lld.lld.musicStreaming.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Album {
    private String albumId;
    private String name;
    private List<Song> songs;

    public Album(String albumId, String name) {
        this.albumId = albumId;
        this.name = name;
        this.songs = new ArrayList<>();
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    @Override
    public String toString() {
        return "Album{" + "albumId='" + albumId + "', name='" + name + "', songsCount=" + songs.size() + "}";
    }
}

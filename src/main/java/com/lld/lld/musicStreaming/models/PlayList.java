package com.lld.lld.musicStreaming.models;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayList {
    private String name;
    private List<Song> songs;

    public PlayList(String name, List<Song> songs) {
        this.name = name;
        this.songs = songs;
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void removeSong(Song song) {
        songs.remove(song);
    }
}

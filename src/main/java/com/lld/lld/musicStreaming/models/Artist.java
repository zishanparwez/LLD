package com.lld.lld.musicStreaming.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Artist {
    private String artistId;
    private String name;
    private List<Song> songs;

    public Artist(String artistId, String name) {
        this.artistId = artistId;
        this.name = name;
        this.songs = new ArrayList<>();
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    @Override
    public String toString() {
        return "Artist{" + "artistId='" + artistId + "', name='" + name + "'}";
    }
}

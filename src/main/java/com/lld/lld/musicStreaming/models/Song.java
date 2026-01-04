package com.lld.lld.musicStreaming.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Song {
    private String songId;
    private String title;
    private Double duration;
    private String artistId;

    public Song(String songId, String title, Double duration, String artistId) {
        this.songId = songId;
        this.title = title;
        this.duration = duration;
        this.artistId = artistId;
    }

    @Override
    public String toString() {
        return "Song{" + "songId='" + songId + "', title='" + title + "', duration=" + duration + "}";
    }
}

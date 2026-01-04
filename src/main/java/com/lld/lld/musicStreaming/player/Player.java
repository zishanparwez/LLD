package com.lld.lld.musicStreaming.player;

import java.util.LinkedList;

import com.lld.lld.musicStreaming.models.Song;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player {
    private PlayerState playerState;
    private LinkedList<Song> queue;
    private Song currentSong;
    private int currentPosition; // in seconds

    public Player() {
        this.queue = new LinkedList<>();
        this.playerState = PlayerState.STOPPED;
        this.currentPosition = 0;
    }

    public void addToQueue(Song song) {
        queue.add(song);
        System.out.println("Added to queue: " + song.getTitle());
    }

    public void play() {
        if (playerState == PlayerState.PAUSED) {
            setPlayerState(PlayerState.PLAYING);
            System.out.println("Resuming: " + currentSong.getTitle() + " from position " + currentPosition + "s");
        } else if (playerState == PlayerState.STOPPED || currentSong == null) {
            if (!queue.isEmpty()) {
                currentSong = queue.poll();
                currentPosition = 0;
                setPlayerState(PlayerState.PLAYING);
                System.out.println("Now playing: " + currentSong.getTitle());
            } else {
                System.out.println("Queue is empty! Add songs to play.");
            }
        } else if (playerState == PlayerState.PLAYING) {
            System.out.println("Already playing: " + currentSong.getTitle());
        }
    }

    public void pause() {
        if (playerState == PlayerState.PLAYING) {
            setPlayerState(PlayerState.PAUSED);
            System.out.println("Paused: " + currentSong.getTitle() + " at position " + currentPosition + "s");
        } else {
            System.out.println("Player is not in playing state!");
        }
    }

    public void seek(int positionInSeconds) {
        if (currentSong != null) {
            if (positionInSeconds >= 0 && positionInSeconds <= currentSong.getDuration()) {
                this.currentPosition = positionInSeconds;
                System.out.println("Seeked to " + positionInSeconds + "s in " + currentSong.getTitle());
            } else {
                System.out.println("Invalid position! Song duration is " + currentSong.getDuration() + "s");
            }
        } else {
            System.out.println("No song is currently loaded!");
        }
    }

    public void skip() {
        if (!queue.isEmpty()) {
            Song previousSong = currentSong;
            currentSong = queue.poll();
            currentPosition = 0;
            setPlayerState(PlayerState.PLAYING);
            System.out.println("Skipped from: " + (previousSong != null ? previousSong.getTitle() : "nothing")
                    + " to: " + currentSong.getTitle());
        } else {
            System.out.println("No more songs in queue to skip to!");
            stop();
        }
    }

    public void stop() {
        setPlayerState(PlayerState.STOPPED);
        currentPosition = 0;
        if (currentSong != null) {
            System.out.println("Stopped: " + currentSong.getTitle());
        }
        currentSong = null;
    }

    public void displayQueue() {
        System.out.println("\n--- Current Queue ---");
        if (currentSong != null) {
            System.out.println("Now Playing: " + currentSong.getTitle() + " [" + playerState + "]");
        }
        if (queue.isEmpty()) {
            System.out.println("Queue is empty.");
        } else {
            int i = 1;
            for (Song song : queue) {
                System.out.println(i++ + ". " + song.getTitle());
            }
        }
        System.out.println("---------------------\n");
    }
}

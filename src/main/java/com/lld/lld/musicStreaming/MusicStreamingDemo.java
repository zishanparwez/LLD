package com.lld.lld.musicStreaming;

import java.util.List;

import com.lld.lld.musicStreaming.facade.MusicStramingFacade;
import com.lld.lld.musicStreaming.models.Artist;
import com.lld.lld.musicStreaming.models.Song;
import com.lld.lld.musicStreaming.models.User;

/**
 * Demonstration of the Music Streaming System
 * Shows usage of Facade Pattern (MusicStramingFacade) and Strategy Pattern
 * (Search and Recommendation)
 */
public class MusicStreamingDemo {
    public static void main(String[] args) {
        // Get the Music Streaming System instance (Singleton Facade)
        MusicStramingFacade musicSystem = MusicStramingFacade.getMusicInstance();

        System.out.println("=".repeat(60));
        System.out.println("MUSIC STREAMING SYSTEM DEMO");
        System.out.println("=".repeat(60));

        // ======================== SETUP: Register Artists ========================
        System.out.println("\n--- Registering Artists ---");
        Artist artist1 = new Artist("ART001", "Taylor Swift");
        Artist artist2 = new Artist("ART002", "Ed Sheeran");
        Artist artist3 = new Artist("ART003", "Coldplay");

        musicSystem.registerArtist(artist1);
        musicSystem.registerArtist(artist2);
        musicSystem.registerArtist(artist3);

        // ======================== SETUP: Add Songs ========================
        System.out.println("\n--- Adding Songs to Catalog ---");

        // Taylor Swift songs
        Song song1 = new Song("SONG001", "Shake It Off", 219.0, "ART001");
        Song song2 = new Song("SONG002", "Blank Space", 231.0, "ART001");
        Song song3 = new Song("SONG003", "Love Story", 235.0, "ART001");

        // Ed Sheeran songs
        Song song4 = new Song("SONG004", "Shape of You", 234.0, "ART002");
        Song song5 = new Song("SONG005", "Perfect", 263.0, "ART002");
        Song song6 = new Song("SONG006", "Thinking Out Loud", 281.0, "ART002");

        // Coldplay songs
        Song song7 = new Song("SONG007", "Yellow", 269.0, "ART003");
        Song song8 = new Song("SONG008", "Fix You", 295.0, "ART003");
        Song song9 = new Song("SONG009", "Viva La Vida", 242.0, "ART003");

        musicSystem.addSong(song1);
        musicSystem.addSong(song2);
        musicSystem.addSong(song3);
        musicSystem.addSong(song4);
        musicSystem.addSong(song5);
        musicSystem.addSong(song6);
        musicSystem.addSong(song7);
        musicSystem.addSong(song8);
        musicSystem.addSong(song9);

        // ======================== SETUP: Register Users ========================
        System.out.println("\n--- Registering Users ---");
        User user1 = new User("USER001", "Alice", "alice@example.com");
        User user2 = new User("USER002", "Bob", "bob@example.com");

        musicSystem.registerUser(user1);
        musicSystem.registerUser(user2);

        // ======================== DEMO: Search Functionality ========================
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO: SEARCH FUNCTIONALITY (Strategy Pattern)");
        System.out.println("=".repeat(60));

        // Search by title (default strategy)
        System.out.println("\n--- Search by Title: 'Love' ---");
        List<Song> searchResults = musicSystem.searchByTitle("Love");
        printSongs(searchResults);

        // Search by artist
        System.out.println("\n--- Search by Artist: Ed Sheeran (ART002) ---");
        searchResults = musicSystem.searchByArtist("ART002");
        printSongs(searchResults);

        // ======================== DEMO: Player Functionality ========================
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO: PLAYER FUNCTIONALITY");
        System.out.println("=".repeat(60));

        // Add songs to queue
        System.out.println("\n--- Adding songs to queue ---");
        musicSystem.addToQueue(song1);
        musicSystem.addToQueue(song4);
        musicSystem.addToQueue(song7);

        // Display queue
        musicSystem.displayQueue();

        // Play
        System.out.println("\n--- Playing songs ---");
        musicSystem.playSong();

        // Pause
        musicSystem.pauseSong();

        // Seek
        musicSystem.seekSong(60);

        // Resume
        musicSystem.playSong();

        // Skip to next
        System.out.println("\n--- Skipping to next song ---");
        musicSystem.skipSong();

        // Display updated queue
        musicSystem.displayQueue();

        // ======================== DEMO: Recommendation Functionality
        // ========================
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO: RECOMMENDATION FUNCTIONALITY (Strategy Pattern)");
        System.out.println("=".repeat(60));

        // User follows artists
        System.out.println("\n--- Alice follows Taylor Swift and Ed Sheeran ---");
        musicSystem.followArtist("USER001", "ART001");
        musicSystem.followArtist("USER001", "ART002");

        // Record some listening history
        System.out.println("\n--- Recording listening history for Alice ---");
        musicSystem.recordListening("USER001", "SONG001");
        musicSystem.recordListening("USER001", "SONG004");

        // Get recommendations by followed artists
        System.out.println("\n--- Recommendations based on Followed Artists ---");
        List<Song> recommendations = musicSystem.getRecomendationByFollowedArtists("USER001");
        printSongs(recommendations);

        // Get recommendations by listening history
        System.out.println("\n--- Recommendations based on Listening History ---");
        recommendations = musicSystem.getRecomendationByListeningHistory("USER001");
        printSongs(recommendations);

        // ======================== DEMO: Stop Player ========================
        System.out.println("\n--- Stopping player ---");
        musicSystem.stopSong();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMO COMPLETE");
        System.out.println("=".repeat(60));
    }

    /**
     * Helper method to print a list of songs
     */
    private static void printSongs(List<Song> songs) {
        if (songs.isEmpty()) {
            System.out.println("  No songs found.");
        } else {
            for (Song song : songs) {
                System.out.println("  - " + song.getTitle() + " (ID: " + song.getSongId() + ")");
            }
        }
    }
}

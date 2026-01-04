package com.lld.lld.musicStreaming.facade;

import java.util.List;

import com.lld.lld.musicStreaming.models.Artist;
import com.lld.lld.musicStreaming.models.Song;
import com.lld.lld.musicStreaming.models.User;
import com.lld.lld.musicStreaming.player.Player;
import com.lld.lld.musicStreaming.recomendation.RecomendByFollowedArtist;
import com.lld.lld.musicStreaming.recomendation.RecomendByListingHistory;
import com.lld.lld.musicStreaming.recomendation.RecomendataionService;
import com.lld.lld.musicStreaming.recomendation.RecomendationStrategy;
import com.lld.lld.musicStreaming.repository.ArtistRepository;
import com.lld.lld.musicStreaming.repository.SongRepository;
import com.lld.lld.musicStreaming.repository.UserRepository;
import com.lld.lld.musicStreaming.search.SearchByArtist;
import com.lld.lld.musicStreaming.search.SearchByTitle;
import com.lld.lld.musicStreaming.search.SearchService;
import com.lld.lld.musicStreaming.search.SearchStrategy;

/**
 * Facade Pattern - Provides a simplified interface to the music streaming
 * subsystem
 * Hides the complexity of Player, SearchService, RecomendataionService, and
 * Repositories
 */
public class MusicStramingFacade {

    private RecomendataionService recomendataionService;
    private SearchService searchService;
    private Player player;

    // Repositories
    private SongRepository songRepository;
    private UserRepository userRepository;
    private ArtistRepository artistRepository;

    private static MusicStramingFacade musicInstance;

    /**
     * Singleton pattern for getting the music streaming system instance
     */
    public static MusicStramingFacade getMusicInstance() {
        if (musicInstance == null) {
            musicInstance = new MusicStramingFacade(
                    new RecomendataionService(),
                    new SearchService(),
                    new Player());
        }
        return musicInstance;
    }

    public MusicStramingFacade(RecomendataionService rs, SearchService ss, Player p) {
        this.recomendataionService = rs;
        this.searchService = ss;
        this.player = p;
        this.songRepository = SongRepository.getInstance();
        this.userRepository = UserRepository.getInstance();
        this.artistRepository = ArtistRepository.getInstance();
    }

    // ======================== Registration Methods ========================

    /**
     * Register a new user in the system
     */
    public void registerUser(User user) {
        userRepository.addUser(user);
        System.out.println("Registered user: " + user.getName());
    }

    /**
     * Register a new artist in the system
     */
    public void registerArtist(Artist artist) {
        artistRepository.addArtist(artist);
        System.out.println("Registered artist: " + artist.getName());
    }

    /**
     * Add a new song to the catalog
     */
    public void addSong(Song song) {
        songRepository.addSong(song);
        // Also add to the artist's song list
        Artist artist = artistRepository.getArtist(song.getArtistId());
        if (artist != null) {
            artist.addSong(song);
        }
        System.out.println("Added song: " + song.getTitle());
    }

    // ======================== Search Methods ========================

    /**
     * Search songs by title (default search)
     */
    public List<Song> search(String query) {
        return searchService.search(query);
    }

    /**
     * Search songs by title
     */
    public List<Song> searchByTitle(String title) {
        searchService.setSearchStrategy(new SearchByTitle());
        return searchService.search(title);
    }

    /**
     * Search songs by artist
     */
    public List<Song> searchByArtist(String artistId) {
        searchService.setSearchStrategy(new SearchByArtist());
        return searchService.search(artistId);
    }

    /**
     * Set a custom search strategy
     */
    public void setSearchStrategy(SearchStrategy strategy) {
        searchService.setSearchStrategy(strategy);
    }

    // ======================== Recommendation Methods ========================

    /**
     * Get song recommendations for a user (using current strategy)
     */
    public List<Song> getRecomendation(String userId) {
        return recomendataionService.getRecomendation(userId);
    }

    /**
     * Get recommendations based on listening history
     */
    public List<Song> getRecomendationByListeningHistory(String userId) {
        recomendataionService.setRecomendationStrategy(new RecomendByListingHistory());
        return recomendataionService.getRecomendation(userId);
    }

    /**
     * Get recommendations based on followed artists
     */
    public List<Song> getRecomendationByFollowedArtists(String userId) {
        recomendataionService.setRecomendationStrategy(new RecomendByFollowedArtist());
        return recomendataionService.getRecomendation(userId);
    }

    /**
     * Set a custom recommendation strategy
     */
    public void setRecomendationStrategy(RecomendationStrategy strategy) {
        recomendataionService.setRecomendationStrategy(strategy);
    }

    // ======================== Player Methods ========================

    /**
     * Add a song to the player queue
     */
    public void addToQueue(Song song) {
        player.addToQueue(song);
    }

    /**
     * Add a song to queue by song ID
     */
    public void addToQueue(String songId) {
        Song song = songRepository.getSong(songId);
        if (song != null) {
            player.addToQueue(song);
        } else {
            System.out.println("Song not found: " + songId);
        }
    }

    /**
     * Play the current song or start from queue
     */
    public void playSong() {
        player.play();
    }

    /**
     * Pause the current song
     */
    public void pauseSong() {
        player.pause();
    }

    /**
     * Skip to the next song
     */
    public void skipSong() {
        player.skip();
    }

    /**
     * Seek to a position in the current song
     */
    public void seekSong(int positionInSeconds) {
        player.seek(positionInSeconds);
    }

    /**
     * Stop the current song
     */
    public void stopSong() {
        player.stop();
    }

    /**
     * Display the current queue
     */
    public void displayQueue() {
        player.displayQueue();
    }

    // ======================== User Actions ========================

    /**
     * Record that a user listened to a song (adds to listening history)
     */
    public void recordListening(String userId, String songId) {
        User user = userRepository.getUser(userId);
        Song song = songRepository.getSong(songId);

        if (user != null && song != null) {
            user.addToListeningHistory(song);
            System.out.println(user.getName() + " listened to: " + song.getTitle());
        }
    }

    /**
     * User follows an artist
     */
    public void followArtist(String userId, String artistId) {
        User user = userRepository.getUser(userId);
        Artist artist = artistRepository.getArtist(artistId);

        if (user != null && artist != null) {
            user.followArtist(artist);
            System.out.println(user.getName() + " is now following: " + artist.getName());
        }
    }

    /**
     * User unfollows an artist
     */
    public void unfollowArtist(String userId, String artistId) {
        User user = userRepository.getUser(userId);
        Artist artist = artistRepository.getArtist(artistId);

        if (user != null && artist != null) {
            user.unfollowArtist(artist);
            System.out.println(user.getName() + " unfollowed: " + artist.getName());
        }
    }

    // ======================== Getter Methods ========================

    public Player getPlayer() {
        return player;
    }

    public Song getSong(String songId) {
        return songRepository.getSong(songId);
    }

    public User getUser(String userId) {
        return userRepository.getUser(userId);
    }

    public Artist getArtist(String artistId) {
        return artistRepository.getArtist(artistId);
    }
}

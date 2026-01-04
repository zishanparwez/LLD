package com.lld.lld.musicStreaming.recomendation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.lld.lld.musicStreaming.models.Artist;
import com.lld.lld.musicStreaming.models.Song;
import com.lld.lld.musicStreaming.models.User;
import com.lld.lld.musicStreaming.repository.ArtistRepository;
import com.lld.lld.musicStreaming.repository.SongRepository;

/**
 * Recommendation strategy based on artists followed by the user
 * Recommends songs from artists the user follows, excluding songs already in
 * listening history
 */
public class RecomendByFollowedArtist implements RecomendationStrategy {

    private SongRepository songRepository;
    private ArtistRepository artistRepository;

    public RecomendByFollowedArtist() {
        this.songRepository = SongRepository.getInstance();
        this.artistRepository = ArtistRepository.getInstance();
    }

    @Override
    public List<Song> getRecomendation(User user) {
        System.out.println("Getting recommendations based on followed artists for: " + user.getName());

        List<Artist> followedArtists = user.getFollowedArtists();
        if (followedArtists == null || followedArtists.isEmpty()) {
            System.out.println("User doesn't follow any artists. No recommendations.");
            return new ArrayList<>();
        }

        // Get songs from followed artists
        Set<String> alreadyListenedIds = user.getListeningHistory().stream()
                .map(Song::getSongId)
                .collect(Collectors.toSet());

        List<Song> recommendations = new ArrayList<>();

        for (Artist artist : followedArtists) {
            List<Song> artistSongs = songRepository.searchByArtistId(artist.getArtistId());
            for (Song song : artistSongs) {
                // Recommend songs user hasn't listened to yet
                if (!alreadyListenedIds.contains(song.getSongId())) {
                    recommendations.add(song);
                }
            }
        }

        System.out.println("Found " + recommendations.size() + " recommendations from followed artists");
        return recommendations;
    }
}

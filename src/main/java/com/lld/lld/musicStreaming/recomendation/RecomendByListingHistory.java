package com.lld.lld.musicStreaming.recomendation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.lld.lld.musicStreaming.models.Song;
import com.lld.lld.musicStreaming.models.User;
import com.lld.lld.musicStreaming.repository.SongRepository;

/**
 * Recommendation strategy based on user's listening history
 * Recommends songs from the same artists the user has listened to before
 */
public class RecomendByListingHistory implements RecomendationStrategy {

    private SongRepository songRepository;

    public RecomendByListingHistory() {
        this.songRepository = SongRepository.getInstance();
    }

    @Override
    public List<Song> getRecomendation(User user) {
        System.out.println("Getting recommendations based on listening history for: " + user.getName());

        List<Song> listeningHistory = user.getListeningHistory();
        if (listeningHistory == null || listeningHistory.isEmpty()) {
            System.out.println("User has no listening history. No recommendations.");
            return new ArrayList<>();
        }

        // Get unique artist IDs from listening history
        Set<String> artistIds = listeningHistory.stream()
                .map(Song::getArtistId)
                .collect(Collectors.toSet());

        Set<String> alreadyListenedIds = listeningHistory.stream()
                .map(Song::getSongId)
                .collect(Collectors.toSet());

        // Get all songs from those artists
        List<Song> recommendations = new ArrayList<>();

        for (String artistId : artistIds) {
            List<Song> artistSongs = songRepository.searchByArtistId(artistId);
            for (Song song : artistSongs) {
                // Recommend songs user hasn't listened to yet
                if (!alreadyListenedIds.contains(song.getSongId())) {
                    recommendations.add(song);
                }
            }
        }

        System.out.println("Found " + recommendations.size() + " recommendations based on listening history");
        return recommendations;
    }
}

package de.gregord.drlleaderboardbackend.event;

import de.gregord.drlleaderboardbackend.cache.KeyGeneratorOverallranking;
import de.gregord.drlleaderboardbackend.entities.LeaderboardEntry;
import de.gregord.drlleaderboardbackend.entities.MapCategory;
import de.gregord.drlleaderboardbackend.entities.Track;
import org.cache2k.CacheEntry;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static de.gregord.drlleaderboardbackend.config.CacheConfig.*;

@Component
public class CacheEventListener {
    private final CacheManager cacheManager;

    public CacheEventListener(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @TransactionalEventListener(value = TrackLeaderboardUpdateEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void onTrackLeaderboardUpdateEvent(TrackLeaderboardUpdateEvent trackLeaderboardUpdateEvent) {
        Track track = trackLeaderboardUpdateEvent.getTrack();
        List<LeaderboardEntry> updatedLeaderboardEntries = trackLeaderboardUpdateEvent.getUpdatedLeaderboardEntries();
        updatedLeaderboardEntries.stream()
                .map(lbe -> lbe.getPlayer().getPlayerName())
                .collect(Collectors.toSet())
                .forEach(s -> Optional.ofNullable(cacheManager.getCache(CACHE_OFFICIAL_LEADERBOARD_BY_PLAYERNAME))
                        .ifPresent(cache -> cache.evictIfPresent(s))
                );

        if (!updatedLeaderboardEntries.isEmpty()) {
            Optional.ofNullable(cacheManager.getCache(CACHE_LEADERBOARD_BY_TRACK))
                    .ifPresent(cache -> cache.evictIfPresent(track.getId()));
        }

        if (!updatedLeaderboardEntries.isEmpty()) {
            Optional.ofNullable(cacheManager.getCache(CACHE_LATEST_LEADERBOARD_ACTIVITY)).ifPresent(Cache::invalidate);
        }

        boolean top10Entries = updatedLeaderboardEntries.stream()
                .anyMatch(l -> l.getPosition() <= 10L);
        if (top10Entries) {
            Optional.ofNullable(cacheManager.getCache(CACHE_LATEST_LEADERBOARD_ACTIVITY)).ifPresent(Cache::invalidate);
        }

        if (MapCategory.getOfficialCategoriesIds().contains(track.getMapCategoryId())) {
            boolean top100Entries = updatedLeaderboardEntries.stream()
                    .anyMatch(l -> l.getPosition() <= 100L);
            if (top100Entries) {
                Optional.ofNullable((org.cache2k.Cache) cacheManager.getCache(CACHE_OVERALLRANKING).getNativeCache())
                        .map(cache -> (Set<CacheEntry<KeyGeneratorOverallranking.Key, ?>>) cache.entries())
                        .stream()
                        .flatMap(cacheEntries -> cacheEntries.stream())
                        .map(cacheEntry -> cacheEntry.getKey())
                        .filter(key -> key.getMapCategories().contains(track.getMapCategoryId()))
                        .forEach(key -> Optional.ofNullable(cacheManager.getCache(CACHE_OVERALLRANKING))
                                .ifPresent(cache -> cache.evictIfPresent(key))
                        );
            }
        }

        if (MapCategory.getCommunityCategoriesIds().contains(track.getMapCategoryId())) {
            boolean top100Entries = updatedLeaderboardEntries.stream()
                    .anyMatch(l -> l.getPosition() <= 100L);
            if (top100Entries) {
                Optional.ofNullable(cacheManager.getCache(CACHE_OVERALLRANKING_COMMUNITY_CURRENT_SEASON)).ifPresent(Cache::invalidate);
            }
        }
    }
}

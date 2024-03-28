package de.gregord.drlleaderboardbackend.config;

import de.gregord.drlleaderboardbackend.cache.KeyGeneratorOverallranking;
import org.cache2k.extra.spring.SpringCache2kCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration(proxyBeanMethods = false)
@EnableCaching
public class CacheConfig {
    public static final String CACHE_OVERALLRANKING = "overallranking";
    public static final String CACHE_TRACKS = "tracks";
    public static final String CACHE_OFFICIAL_LEADERBOARD_BY_PLAYERNAME = "officialLeaderboardByPlayername";
    public static final String CACHE_LEADERBOARD_BY_TRACK = "leaderboardbytrack";
    public static final String CACHE_LATEST_LEADERBOARD_ACTIVITY = "latestLeaderboardActivity";
    public static final String CACHE_LATEST_LEADERBOARD_ACTIVITY_TOP10 = "latestLeaderboardActivityTop10";
    public static final String CACHE_MOST_PBS_LAST_7DAYS = "mostPbsLast7Days";
    public static final String CACHE_MOST_PBS_LAST_MONTH = "mostPbsLastMonth";
    public static final String CACHE_MOST_ENTRIES_FOR_TRACK_LAST_14DAYS = "mostEntriesByTrackLast14Days";
    public static final String CACHE_MOST_ENTRIES_FOR_TRACK_LAST_MONTH = "mostEntriesByTrackLastMonth";

    public static final String CACHE_TOURNAMENT_RANKINGS = "tournamentRankings";

    public static final String CACHE_OVERALLRANKING_COMMUNITY_CURRENT_SEASON = "communityRankingCurrentSeason";
    // TODO currently not used, only evicted
    public static final String CACHE_COMMUNITY_TRACKS = "communityTracks";
    public static final String CACHE_COMMUNITY_LEADERBOARD_BY_PLAYERNAME_CURRENT_SEASON = "communityLeaderboardByPlayernameCurrentSeason";
    public static final String CACHE_COMMUNITY_CURRENT_SEASON_TRACKIDS = "currentSeasonTrackIds";

    public static final String CACHE_TWITCH_STREAMS = "twitchStreams";

    public static final String CACHE_INTERNAL_MAPID_TRACKID_MAPPING = "internalMapIdTrackIdMapping";
    public static final String CACHE_INTERNAL_LAST_SAVED_PLAYERS = "lastSavedPlayers";

    public static final String CACHE_KEYGENERATOR_OVERALLRANKING = "cacheKeygeneratorOverallranking";

    @Bean("cacheKeygeneratorOverallranking")
    public KeyGenerator createCacheKeygeneratorOverallranking() {
        return new KeyGeneratorOverallranking();
    }

    @Bean
    public CacheManager cacheManager() {
        return new SpringCache2kCacheManager()
                .addCache(c -> c.name(CACHE_OVERALLRANKING)
                        .entryCapacity(8)) //
//                        .weigher((k, v) -> {
//                            if (v instanceof Collection) {
//                                return ((Collection) v).size();
//                            }
//                            return 1;
//                        })
//                        .maximumWeight(50 * 15))
                .addCache(c -> c.name(CACHE_TRACKS)
                        .eternal(true)
                        .entryCapacity(1)
                        .permitNullValues(true))
                // TODO implement some clever cache eviction instead of 1 minute
                .addCache(c -> c.name(CACHE_OFFICIAL_LEADERBOARD_BY_PLAYERNAME)
                        .expireAfterWrite(1, TimeUnit.MINUTES)
                        .entryCapacity(300)
                        .permitNullValues(true))
                // TODO implement some clever cache eviction instead of 1 minute
                .addCache(c -> c.name(CACHE_LEADERBOARD_BY_TRACK)
                        .expireAfterWrite(1, TimeUnit.MINUTES)
                        .entryCapacity(300)
                        .permitNullValues(true))
                .addCache(c -> c.name(CACHE_LATEST_LEADERBOARD_ACTIVITY)
                        .entryCapacity(1)
                        .permitNullValues(true))
                .addCache(c -> c.name(CACHE_LATEST_LEADERBOARD_ACTIVITY_TOP10)
                        .entryCapacity(1)
                        .permitNullValues(true))
                // TODO implement some clever cache eviction instead of 1 minute
                .addCache(c -> c.name(CACHE_MOST_PBS_LAST_7DAYS)
                        .expireAfterWrite(1, TimeUnit.MINUTES)
                        .entryCapacity(1)
                        .permitNullValues(true))
                // TODO implement some clever cache eviction instead of 1 minute
                .addCache(c -> c.name(CACHE_MOST_PBS_LAST_MONTH)
                        .expireAfterWrite(1, TimeUnit.MINUTES)
                        .entryCapacity(1)
                        .permitNullValues(true))
                // TODO implement some clever cache eviction instead of 1 minute
                .addCache(c -> c.name(CACHE_MOST_ENTRIES_FOR_TRACK_LAST_14DAYS)
                        .expireAfterWrite(1, TimeUnit.MINUTES)
                        .entryCapacity(1)
                        .permitNullValues(true))
                // TODO implement some clever cache eviction instead of 1 minute
                .addCache(c -> c.name(CACHE_MOST_ENTRIES_FOR_TRACK_LAST_MONTH)
                        .expireAfterWrite(1, TimeUnit.MINUTES)
                        .entryCapacity(1)
                        .permitNullValues(true))
                .addCache(c -> c.name(CACHE_TOURNAMENT_RANKINGS)
                        .eternal(true)
                        .entryCapacity(1)
                        .permitNullValues(true))

                // COMMUNITY RELATED:
                .addCache(c -> c.name(CACHE_COMMUNITY_TRACKS)
                        .eternal(true)
                        .entryCapacity(1)
                        .permitNullValues(true))
                .addCache(c -> c.name(CACHE_OVERALLRANKING_COMMUNITY_CURRENT_SEASON)
                        .eternal(true)
                        .entryCapacity(1)
                        .permitNullValues(false))
                // TODO implement some clever cache eviction instead of 1 Minute
                .addCache(c -> c.name(CACHE_COMMUNITY_LEADERBOARD_BY_PLAYERNAME_CURRENT_SEASON)
                        .expireAfterWrite(1, TimeUnit.MINUTES)
                        .entryCapacity(300)
                        .permitNullValues(true))
                .addCache(c -> c.name(CACHE_COMMUNITY_CURRENT_SEASON_TRACKIDS)
                        .expireAfterWrite(1, TimeUnit.DAYS)
                        .entryCapacity(1)
                        .permitNullValues(false))

                // Twitch
                .addCache(c -> c.name(CACHE_TWITCH_STREAMS)
                        .expireAfterWrite(1, TimeUnit.MINUTES)
                        .entryCapacity(1)
                        .permitNullValues(false))
                // Internal caches
                .addCache(c -> c.name(CACHE_INTERNAL_MAPID_TRACKID_MAPPING)
                        .expireAfterWrite(1, TimeUnit.HOURS)
                        .entryCapacity(1)
                        .permitNullValues(false))
                .addCache(c -> c.name(CACHE_INTERNAL_LAST_SAVED_PLAYERS)
                        .entryCapacity(1000)

                        .permitNullValues(false));

    }
}

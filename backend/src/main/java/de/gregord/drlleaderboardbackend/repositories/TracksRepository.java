package de.gregord.drlleaderboardbackend.repositories;

import de.gregord.drlleaderboardbackend.domain.LeaderboardByPlayerView;
import de.gregord.drlleaderboardbackend.domain.TrackCommunityView;
import de.gregord.drlleaderboardbackend.domain.TrackView;
import de.gregord.drlleaderboardbackend.domain.TrackWeightedRatingView;
import de.gregord.drlleaderboardbackend.entities.Track;
import de.gregord.drlleaderboardbackend.services.TracksService;
import jakarta.persistence.QueryHint;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static de.gregord.drlleaderboardbackend.config.CacheConfig.CACHE_TRACKS;

@Repository
public interface TracksRepository extends JpaRepository<Track, Long> {
    Optional<Track> findByGuid(String guid);

    Collection<Track> findByGuidIn(Collection<String> guids);

    int countByMapCategoryIdIn(Set<Integer> mapCategoryIds);

    @Query("""
                SELECT t from Track t
                    LEFT JOIN CommunitySeason cs on cs.track = t
                WHERE t.mapCategoryId in (:mapCategoryIds) or (cs.excluded = false and cs.seasonId = :seasonId)
            """)
    @Cacheable(value = CACHE_TRACKS)
    @Transactional(readOnly = true)
    List<TrackView> getAllActiveTracks(Set<Integer> mapCategoryIds, int seasonId, Sort sort);

    @Query(value = """
            SELECT t.id              as id,
                   t.name            as name,
                   t.map_name        as mapName,
                   t.parent_category as parentCategory
            FROM tracks t
            WHERE t.map_category_id in (:mapCategoryIds) AND NOT EXISTS (SELECT 1
                              FROM leaderboards l
                                       INNER JOIN players p ON p.id = l.player_id
                              WHERE l.track_id = t.id
                                AND p.player_name = :playerName)
            ORDER BY t.map_name, t.parent_category, t.name;
                """, nativeQuery = true)
    List<LeaderboardByPlayerView.LeaderboardByPlayerView_Track> findMissingTracksByPlayerName(Set<Integer> mapCategoryIds,
                                                                                              String playerName);

    @Query(value = """
            SELECT t.id              as id,
                   t.name            as name,
                   t.map_name        as mapName,
                   t.parent_category as parentCategory
            FROM (SELECT * FROM community_seasons cs_i WHERE cs_i.season_id = :seasionId AND excluded = FALSE ) cs
                INNER JOIN tracks t on t.id = cs.track_id
            WHERE NOT EXISTS (SELECT 1
                              FROM leaderboards l
                                       INNER JOIN players p ON p.id = l.player_id
                              WHERE l.track_id = t.id
                                AND p.player_name = :playerName)
            ORDER BY t.map_name, t.parent_category, t.name;
                """, nativeQuery = true)
    List<LeaderboardByPlayerView.LeaderboardByPlayerView_Track> findMissingTracksForCurrentSeasonByPlayerName(int seasionId,
                                                                                                              String playerName);

    Collection<Track> findByIdNotInAndMapCategoryIdIn(Collection<Long> ids, Set<Integer> mapCategoryIds);

    @Query(value = """
                WITH AverageRating AS (SELECT AVG(rating_score) AS avg_rating_score
                                       FROM tracks
                                       WHERE created_at >= :lowerLimit
                                         AND created_at < :upperLimit
                                         AND map_category_id = :mapCategoryId),
                     TournamentBoost AS (SELECT custom_map, COUNT(guid) * 0.1 AS boost
                                         FROM tournaments
                                         GROUP BY custom_map),
                     WeightedRatings AS (SELECT t.*,
                                                (
                                                    (t.rating_count / (t.rating_count + 20.0)) * t.rating_score +
                                                    (20.0 / (t.rating_count + 20.0)) * (SELECT avg_rating_score FROM AverageRating)
                                                    ) +
                                                COALESCE(tb.boost, 0) AS weighted_rating, -- Adjusting weighted rating based on tournament presence and count
                                                tb.boost              AS tournamentBoost  -- Indicating whether the rating was boosted
                                         FROM tracks t
                                                  LEFT JOIN TournamentBoost tb ON t.guid = tb.custom_map)
                SELECT id,
                       guid,
                       map_id                          AS mapId,
                       drl_track_id                    AS drlTrackId,
                       name,
                       track_creator                   AS trackCreator,
                       map_difficulty                  AS mapDifficulty,
                       rating_count                    AS ratingCount,
                       rating_score                    AS ratingScore,
                       weighted_rating                 AS weightedRating,
                       WeightedRatings.tournamentBoost as tournamentBoost
                FROM WeightedRatings
                WHERE created_at >= :lowerLimit
                  AND created_at < :upperLimit
                  AND map_category_id = :mapCategoryId
                ORDER BY weighted_rating DESC NULLS LAST;
            """, nativeQuery = true)
    Collection<TrackWeightedRatingView> getTracksForDateRangeOrderedByRating(
            Integer mapCategoryId,
            LocalDateTime lowerLimit,
            LocalDateTime upperLimit);

    @Query(value = """
                WITH AverageRating AS (SELECT AVG(rating_score) AS avg_rating_score
                                       FROM tracks
                                       WHERE map_category_id = :mapCategoryId),
                
                     WeightedRatings AS (SELECT t.*,
                                                (
                                                    (t.rating_count / (t.rating_count + 50.0)) * t.rating_score +
                                                    (50.0 / (t.rating_count + 50.0)) * (SELECT avg_rating_score FROM AverageRating)
                                                    ) AS weighted_rating
                                         FROM tracks t)
                SELECT id,
                       guid,
                       map_id          AS mapId,
                       drl_track_id    AS drlTrackId,
                       name,
                       track_creator   AS trackCreator,
                       map_difficulty  AS mapDifficulty,
                       rating_count    AS ratingCount,
                       rating_score    AS ratingScore,
                       weighted_rating AS weightedRating
                FROM WeightedRatings
                WHERE map_category_id = :mapCategoryId
                ORDER BY weighted_rating DESC NULLS LAST;
            """, nativeQuery = true)
    @QueryHints(value = @QueryHint(name = "jakarta.persistence.fetch.size", value = "50"))
    Stream<TrackWeightedRatingView> getTracksOrderedByRating(Integer mapCategoryId);

    @Query(value = """
                SELECT AVG(rating_score) AS avg_rating_score
                FROM tracks
                WHERE map_category_id = :mapCategoryId
            """, nativeQuery = true)
    Double getAverageRating(Integer mapCategoryId);

    @Query(value = """
                SELECT AVG(rating_score) AS avg_rating_score
                FROM tracks
                WHERE created_at >= :lowerLimit AND created_at < :upperLimit AND map_category_id = :mapCategoryId
            """, nativeQuery = true)
    Double getAverageRatingWithinDateRange(Integer mapCategoryId,
                                           LocalDateTime lowerLimit,
                                           LocalDateTime upperLimit);

    @Query(value = """
                SELECT filtered_tracks.id
                FROM (
                         SELECT t.id
                         FROM tracks t
                         WHERE t.map_category_id IN (0,1,2,3,4,5)
                         UNION ALL
                         SELECT t.id
                         FROM community_seasons cs
                                  INNER JOIN tracks t ON cs.track_id = t.id
                         WHERE cs.season_id = :seasonId and cs.excluded = FALSE
                     ) AS filtered_tracks
                LEFT JOIN (
                    SELECT l.track_id, MAX(l.updated_at) AS max_updated_at
                    FROM leaderboards l
                    GROUP BY l.track_id
                ) AS l ON l.track_id = filtered_tracks.id
                ORDER BY l.max_updated_at DESC NULLS FIRST;
            """, nativeQuery = true)
    List<Long> getTracksToBeUpdatedByPriority(int seasonId);

    @Query(value = """
            SELECT t.map_id AS mapId, t.drl_track_id AS drlTrackId
                FROM tracks t
                WHERE t.drl_track_id != t.guid -- this is needed because the very old offical tracks hav different trackid for the map_ids because they used the guid as track id
                GROUP BY t.map_id, t.drl_track_id
            """, nativeQuery = true)
    List<TracksService.MapIdAndTrackIdView> findDistinctMapIdAndDrlTrackId();

    @Query(value = """
                SELECT t.id as id,
                    t.name AS name,
                    t.map_name AS mapName,
                    t.parent_category AS parentCategory,
                    t.track_creator as trackCreator
                FROM tracks t
                    INNER JOIN community_seasons cs on cs.track_id = t.id
                WHERE cs.excluded = false;
            """, nativeQuery = true)
    Collection<TrackCommunityView> findTracksBySeasonId(int seasonId);
}

package de.gregord.drlleaderboardbackend.repositories;

import de.gregord.drlleaderboardbackend.domain.*;
import de.gregord.drlleaderboardbackend.entities.LeaderboardEntry;
import de.gregord.drlleaderboardbackend.entities.TrackMinimal;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static de.gregord.drlleaderboardbackend.config.CacheConfig.*;

@Repository
public interface LeaderboardRepository extends JpaRepository<LeaderboardEntry, Long> {
    @EntityGraph(attributePaths = {"player"})
    @Transactional(readOnly = true)
    Collection<LeaderboardEntry> findByTrack(TrackMinimal track);

    // The @EntityGraph annotation results in duplicate join statements of the beatenBy association so JPQL is used here
    @Query("""
            SELECT l
            FROM LeaderboardEntry l
                JOIN FETCH l.track track
                JOIN FETCH l.player player
                LEFT JOIN FETCH l.beatenBy beatenBy
                LEFT JOIN FETCH beatenBy.player bbPlayer
            WHERE player.playerName = :playerName and track.mapCategoryId in (:mapCategoryIds)
            """)
    List<LeaderboardByPlayerView> findByPlayerNameAndMapCategoryIdsIn(String playerName, Collection<Integer> mapCategoryIds, Sort sort);

    @Query("""
            SELECT l
            FROM LeaderboardEntry l
                JOIN FETCH l.track track
                JOIN FETCH l.player player
                LEFT JOIN FETCH l.beatenBy beatenBy
                LEFT JOIN FETCH beatenBy.player bbPlayer
            WHERE player.playerName = :playerName and track.id in (:trackIds)
            """)
    List<LeaderboardByPlayerView> findByTrackIdInAndPlayerName(List<Long> trackIds, String playerName, Sort sort);

    @EntityGraph(attributePaths = {"player"})
    Collection<LeaderboardEntry> findByTrackIdAndPlayerIdDrlIn(Long trackId, Collection<String> playerIds);

    List<ReplaysByTrackView> findByTrackIdAndIsInvalidRunFalse(Long trackId, Sort sort);

    List<LeaderboardEntry> findByTrackId(Long trackId);

    @Modifying
    @Query(value = """
            DELETE FROM leaderboards_beaten_by
            WHERE leaderboard_id = :leaderboardId or beaten_by_leaderboard_id = :leaderboardId
            """, nativeQuery = true)
    void deleteBeatenByEntriesByLeaderboardId(Long leaderboardId);

    @Cacheable(value = CACHE_OVERALLRANKING, keyGenerator = CACHE_KEYGENERATOR_OVERALLRANKING)
    @Query(value = """
            WITH overall_ranking as (SELECT l.player_id              AS playerId,
                                            p.player_name              AS playerName,
                                            round(sum(l.points))       AS totalPoints,
                                            avg(l.position)            AS avgPosition,
                                            count(*)                 AS completedTracks,
                                            sum(l.crash_count)         AS totalCrashCount,
                                            max(l.top_speed)           AS maxTopSpeed,
                                            sum(l.score)               AS totalScore,
                                            min(p.flag_url)            AS flagUrl,
                                            min(p.profile_platform) AS profilePlatform,
                                            min(p.profile_thumb)       AS profileThumb,
                                            max(l.updated_at)        AS latestActivity
                                     FROM leaderboards l
                                        INNER JOIN tracks t ON t.id = l.track_id
                                        INNER JOIN players p ON p.id = l.player_id
                                     WHERE is_invalid_run = FALSE AND
                                        t.map_category_id in (:mapCategoryIds)
                                     GROUP BY l.player_id, player_name
                                     ORDER BY sum(points) DESC
                                     limit :limit OFFSET :offset),
                 invalid_runs AS (SELECT l.player_id,
                                    COALESCE(count(*), 0) as invalid_runs
                                  FROM leaderboards l
                                    INNER JOIN tracks t ON t.id = l.track_id
                                    INNER JOIN overall_ranking ovr ON l.player_id = ovr.playerId
                                  WHERE l.is_invalid_run = true AND
                                    t.map_category_id in (:mapCategoryIds)
                                  GROUP BY l.player_id)
            SELECT ROW_NUMBER() OVER (ORDER BY totalPoints DESC)                                                 as position,
                   playerName,
                   totalPoints,
                   avgPosition,
                   COALESCE((select invalid_runs from invalid_runs ir where ir.player_id = ovr.playerId), 0) as invalidRuns,
                   completedTracks,
                   totalCrashCount,
                   totalScore,
                   maxTopSpeed,
                   substring(flagUrl from position('.png' in flagUrl) - 2 for 2) as flagUrl,
                   profilePlatform,
                   profileThumb,
                   latestActivity
            from overall_ranking ovr;
                        """, nativeQuery = true)
    List<OverallRankingView> getOverallRanking(Set<Integer> mapCategoryIds, int limit, int offset);

    //    @Query("""
//        SELECT l
//        FROM LeaderboardEntry l
//            JOIN FETCH l.track track
//        """)
    @Cacheable(value = CACHE_LEADERBOARD_BY_TRACK, key = "#trackId")
    List<LeaderboardByTrackView> findByTrackId(Long trackId, Pageable pageable);

    @Cacheable(CACHE_LATEST_LEADERBOARD_ACTIVITY)
    @Query(value = """
            SELECT
                p.player_name as playerName,
                l.position,
                l.created_at as createdAt,
                t.id as trackId,
                t.name as trackName,
                t.map_name as mapName,
                t.parent_category as parentCategory
            FROM leaderboards l
                     INNER JOIN tracks t ON t.id = l.track_id
                     INNER JOIN players p ON p.id = l.player_id
            WHERE l.is_invalid_run = false
            ORDER BY l.created_at DESC
            LIMIT 10
            """, nativeQuery = true)
    List<LeaderboardActivityView> latestLeaderboardActivity();

    @Cacheable(CACHE_LATEST_LEADERBOARD_ACTIVITY_TOP10)
    @Query(value = """
            SELECT
                p.player_name as playerName,
                l.position,
                l.created_at as createdAt,
                t.id as trackId,
                t.name as trackName,
                t.map_name as mapName,
                t.parent_category as parentCategory
            FROM leaderboards l
                     INNER JOIN tracks t ON t.id = l.track_id
                     INNER JOIN players p ON p.id = l.player_id
            WHERE l.position <= 10 and l.is_invalid_run = false
            ORDER BY l.created_at DESC
            LIMIT 10
            """, nativeQuery = true)
    List<LeaderboardActivityView> latestLeaderboardActivityTop10();

    @Cacheable(CACHE_MOST_PBS_LAST_7DAYS)
    @Query(value = """
            SELECT
                p.player_name as playerName,
                count(*) as entries,
                min(l.position) as bestPosition,
                round(avg(l.position)) as avgPosition
            FROM leaderboards l
                     INNER JOIN tracks t ON t.id = l.track_id
                     INNER JOIN players p ON p.id = l.player_id
            WHERE l.created_at > (now() - INTERVAL '7' DAY)
              AND l.is_invalid_run = false
            GROUP BY p.player_name
            ORDER BY count(*) DESC
            LIMIT 10
            """, nativeQuery = true)
    List<LeaderboardMostPbsView> mostPbsLast7Days();

    @Cacheable(CACHE_MOST_PBS_LAST_MONTH)
    @Query(value = """
            SELECT
                p.player_name as playerName,
                count(*) as entries,
                min(l.position) as bestPosition,
                round(avg(l.position)) as avgPosition
            FROM leaderboards l
                     INNER JOIN tracks t ON t.id = l.track_id
                     INNER JOIN players p ON p.id = l.player_id
            WHERE l.created_at > (now() - INTERVAL '1' MONTH)
              AND l.is_invalid_run = false
            GROUP BY p.player_name
            ORDER BY count(*) DESC
            LIMIT 10
            """, nativeQuery = true)
    List<LeaderboardMostPbsView> mostPbsLastMonth();

    @Cacheable(CACHE_MOST_ENTRIES_FOR_TRACK_LAST_MONTH)
    @Query(value = """
            SELECT
                t.id as id,
                t.name as name,
                t.map_name as mapName,
                t.parent_category as parentCategory,
                count(*) as entries
            FROM leaderboards l
                     LEFT JOIN tracks t ON t.id = l.track_id
            WHERE l.created_at > (now() - INTERVAL '1' MONTH)
              AND l.is_invalid_run = false
            GROUP BY t.id
            ORDER BY count(*) DESC
            LIMIT 10
                """, nativeQuery = true)
    List<LeaderboardMostTrackEntriesView> mostEntriesByTrackLastMonth();

    @Cacheable(CACHE_MOST_ENTRIES_FOR_TRACK_LAST_14DAYS)
    @Query(value = """
            SELECT
                t.id as id,
                t.name as name,
                t.map_name as mapName,
                t.parent_category as parentCategory,
                count(*) as entries
            FROM leaderboards l
                     LEFT JOIN tracks t ON t.id = l.track_id
            WHERE l.created_at > (now() - INTERVAL '14' DAY)
              AND l.is_invalid_run = false
            GROUP BY t.id
            ORDER BY count(*) DESC
            LIMIT 10
                """, nativeQuery = true)
    List<LeaderboardMostTrackEntriesView> mostEntriesByTrackLast14Days();

    //    @Cacheable("worstTrackByPlayer")
    @Query(value = """
            WITH main as (
                SELECT t.id              as track_id,
                    t.name            as track_name,
                    t.map_name        as track_map_name,
                    t.parent_category as track_parent_category,
                    l.id              as leaderboard_id,
                    l.position        as player_position,
                    l.score           as player_score,
                    l.created_at      as player_created_at,
                    l.is_invalid_run  as player_is_invalid_run
                FROM tracks t
                    LEFT JOIN community_seasons cs ON cs.track_id = t.id
                    LEFT JOIN (SELECT id,
                                    track_id,
                                    position,
                                    score,
                                    created_at,
                                    is_invalid_run
                                FROM leaderboards
                                WHERE player_id = (SELECT id FROM players WHERE player_name = :playerName)) l ON t.id = l.track_id
                WHERE
                (t.map_category_id in (:officialMapCategoryIds) OR cs.season_id = :communitySeasonId) AND
                (:excludedTrackIds IS NULL OR t.id NOT IN (:excludedTrackIds)) AND
                (cs.season_id IS NULL OR cs.season_id = :communitySeasonId) AND
                (:onlyIncludeCommunitySeason = FALSE OR cs.season_id IS NOT NULL))
            SELECT trackId,
                   trackName,
                   trackMapName,
                   trackParentCategory,
                   json_agg(reason ORDER BY reason) as reasons FROM (
            """ +
// 10 last maps you didn't improve upon lately
            """
                            
                          (SELECT m.track_id               as trackId,
                                  m.track_name             as trackName,
                                  m.track_map_name         as trackMapName,
                                  m.track_parent_category  as trackParentCategory,
                                  'IMPROVEMENT_IS_LONG_AGO' as reason
                           FROM main m
                           WHERE m.player_is_invalid_run = false AND m.leaderboard_id is not null AND :includeImprovementIsLongAgo
                           ORDER BY m.player_created_at
                           LIMIT 10)
                          UNION
                    """ +
//  10 maps with the worst position compared to your other tracks
            """
                            
                          (SELECT m.track_id              as trackId,
                                  m.track_name            as trackName,
                                  m.track_map_name        as trackMapName,
                                  m.track_parent_category as trackParentCategory,
                                  'WORST_POSITION'       as reason
                           FROM main m
                           WHERE m.player_position > 1
                             AND m.player_is_invalid_run = false
                             AND m.leaderboard_id is not null AND :includeWorstPosition
                           ORDER BY m.player_position DESC
                           LIMIT 10)
                          UNION
                    """ +
//  10 tracks with most beaten by entries and then order by oldest beaten by entry (because 5 is max)
            """
                          (SELECT min(m.track_id)              as trackId,
                                  min(m.track_name)            as trackName,
                                  min(m.track_map_name)        as trackMapName,
                                  min(m.track_parent_category) as trackParentCategory,
                                  'MOST_BEATEN_BY_ENTRIES'     as reason
                           FROM main m
                             INNER JOIN leaderboards_beaten_by b ON b.leaderboard_id = m.leaderboard_id
                             INNER JOIN leaderboards l ON l.id = b.beaten_by_leaderboard_id
                           WHERE m.player_is_invalid_run = false and m.leaderboard_id is not null AND :includeMostBeatenByEntries
                           GROUP BY m.leaderboard_id
                           ORDER BY count(l.player_id) desc, min(l.created_at)
                           limit 10)
                          UNION
                    """ +
//  10 tracks where you are the farest behind the top position
            """
                          (SELECT m.track_id               as trackId,
                                  m.track_name             as trackName,
                                  m.track_map_name         as trackMapName,
                                  m.track_parent_category  as trackParentCategory,
                                  'FARTHEST_BEHIND_LEADER' as reason
                           FROM main m
                            INNER JOIN (SELECT track_id, score
                                        FROM leaderboards l
                                        WHERE l.position = 1
                                          AND l.is_invalid_run = false) l ON l.track_id = m.track_id
                           WHERE m.leaderboard_id is not null AND :includeFarthestBehindLeader
                           ORDER BY m.player_score - l.score DESC
                           LIMIT 10)
                          UNION
                    """ +
// 10 tracks where it is potentially easy to improve (scorediff / position = how many seconds you need to advance a position,
// a higher value is an indicator for easier improvement)
            """
                          (select m.track_id                    as trackId,
                                  m.track_name                  as trackName,
                                  m.track_map_name              as trackMapName,
                                  m.track_parent_category       as trackParentCategory,
                                  'POTENTIALLY_EASY_TO_ADVANCE' as reason
                           FROM main m
                            INNER JOIN (SELECT track_id,
                                               score
                                        FROM leaderboards l
                                        WHERE l.position = 1
                                          AND l.is_invalid_run = false) l ON l.track_id = m.track_id
                           WHERE m.leaderboard_id is not null AND :includePotentiallyEasyToAdvance
                           ORDER BY (m.player_score - l.score) / m.player_position DESC
                           LIMIT 10)
                          UNION
                    """ +
// 10 tracks where your time is invalid
            """
                          (SELECT m.track_id              as trackId,
                                  m.track_name            as trackName,
                                  m.track_map_name        as trackMapName,
                                  m.track_parent_category as trackParentCategory,
                                  'INVALID_RUN'           as reason
                           FROM main m
                           WHERE m.player_is_invalid_run = true AND m.leaderboard_id is not null AND :includeInvalidRuns
                           ORDER BY m.player_position DESC
                           LIMIT 10)
                          UNION
                    """ +
// 10 tracks you didn't complete
            """
                                  (SELECT m.track_id              as trackId,
                                          m.track_name            as trackName,
                                          m.track_map_name        as trackMapName,
                                          m.track_parent_category as trackParentCategory,
                                          'NOT_COMPLETED'           as reason
                                   FROM main m
                                   WHERE m.leaderboard_id IS NULL AND :includeNotCompleted
                                   LIMIT 10)
                            ) unioned
                            GROUP BY trackId, trackName, trackMapName, trackParentCategory
                            ORDER BY count(reason) DESC;
                    """, nativeQuery = true)
    List<WorstTracksView> worstTracksByPlayer(
            String playerName,
            Boolean includeImprovementIsLongAgo,
            Boolean includeWorstPosition,
            Boolean includeMostBeatenByEntries,
            Boolean includeFarthestBehindLeader,
            Boolean includePotentiallyEasyToAdvance,
            Boolean includeInvalidRuns,
            Boolean includeNotCompleted,
            List<Long> excludedTrackIds,
            Collection<Integer> officialMapCategoryIds,
            Integer communitySeasonId,
            Boolean onlyIncludeCommunitySeason);
}

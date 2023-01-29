package de.gregord.drlleaderboardbackend.repositories;

import de.gregord.drlleaderboardbackend.domain.*;
import de.gregord.drlleaderboardbackend.entities.LeaderboardEntry;
import de.gregord.drlleaderboardbackend.entities.TrackMinimal;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeaderboardRepository extends JpaRepository<LeaderboardEntry, Long> {
    Collection<LeaderboardEntry> findByTrack(TrackMinimal track);

    // The @EntityGraph annotation results in duplicate join statements of the beatenBy association so JPQL is used here
    @Query("""
            SELECT l
            FROM LeaderboardEntry l
                JOIN FETCH l.track track
                LEFT JOIN FETCH l.beatenBy beatenBy
            WHERE l.playerName = :playerName
            """)
    @Cacheable(value = "leaderboardbyplayername", key = "#playerName")
    List<LeaderboardByPlayerView> findByPlayerName(String playerName, Sort sort);

    Optional<LeaderboardEntry> findByTrackIdAndPlayerId(Long trackId, String playerId);

    Collection<LeaderboardEntry> findByTrackIdAndPlayerIdIn(Long trackId, Collection<String> playerIds);

    @Cacheable("overallranking")
    @Query(value = """
WITH overall_ranking as (SELECT player_name              as playerName,
                                round(sum(points))       as totalPoints,
                                avg(position)            as avgPosition,
                                count(*)                 as completedTracks,
                                sum(crash_count)         as totalCrashCount,
                                max(top_speed)           as maxTopSpeed,
                                sum(score)               as totalScore,
                                min(flag_url)            as flagUrl,
                                min(profile_platform) as profilePlatform,
                                min(profile_thumb)       as profileThumb
                         FROM leaderboards l
                         where is_invalid_run = false
                         group by player_name
                         order by sum(points) desc
                         limit :limit OFFSET :offset),
     invalid_runs AS (SELECT l.player_name,
                             COALESCE(count(*), 0) as invalid_runs
                      FROM leaderboards l
                               left join overall_ranking ovr on l.player_name = ovr.playerName
                      WHERE l.is_invalid_run = true
                      GROUP BY l.player_name)
SELECT ROW_NUMBER() OVER (ORDER BY totalPoints DESC)                                                 as position,
       playerName,
       totalPoints,
       avgPosition,
       COALESCE((select invalid_runs from invalid_runs ir where ir.player_name = ovr.playerName), 0) as invalidRuns,
       completedTracks,
       totalCrashCount,
       totalScore,
       maxTopSpeed,
       substring(flagUrl from position('.png' in flagUrl) - 2 for 2) as flagUrl,
       profilePlatform,
       profileThumb
from overall_ranking ovr;
            """, nativeQuery = true)
    List<OverallRankingView> getOverallRanking(@Param("limit") int limit, @Param("offset") int offset);

    //    @Query("""
//        SELECT l
//        FROM LeaderboardEntry l
//            JOIN FETCH l.track track
//        """)
    @Cacheable(value = "leaderboardbytrack", key = "#guid")
    List<LeaderBoardByTrackView> findByTrackId(Long guid, Pageable pageable);

    @Query("""
            SELECT DISTINCT l.playerName
            FROM LeaderboardEntry l
            WHERE l.playerName like :playerName
            """)
    List<String> findDistinctPlayerNames(String playerName, Pageable pageable);

    @Cacheable("latestLeaderboardActivity")
    @Query(value = """
            SELECT
                l.player_name as playerName,
                l.position,
                l.created_at as createdAt,
                t.name as trackName,
                t.map_name as mapName,
                t.parent_category as parentCategory
            FROM leaderboards l
                     LEFT JOIN tracks t ON t.id = l.track_id
            WHERE l.is_invalid_run = false
            ORDER BY l.created_at DESC
            LIMIT 10
            """, nativeQuery = true)
    List<LeaderboardActivityView> latestLeaderboardActivity();

    @Cacheable("latestLeaderboardActivityTop10")
    @Query(value = """
            SELECT
                l.player_name as playerName,
                l.position,
                l.created_at as createdAt,
                t.name as trackName,
                t.map_name as mapName,
                t.parent_category as parentCategory
            FROM leaderboards l
                     LEFT JOIN tracks t ON t.id = l.track_id
            WHERE l.position <= 10 and l.is_invalid_run = false
            ORDER BY l.created_at DESC
            LIMIT 10
            """, nativeQuery = true)
    List<LeaderboardActivityView> latestLeaderboardActivityTop10();

    @Cacheable("mostPbsLast7Days")
    @Query(value = """
            SELECT
                l.player_name as playerName,
                count(*) as entries,
                min(l.position) as bestPosition,
                round(avg(l.position)) as avgPosition
            FROM leaderboards l
                     LEFT JOIN tracks t ON t.id = l.track_id
            WHERE l.created_at > (now() - INTERVAL '7' DAY)
              AND l.is_invalid_run = false
            GROUP BY l.player_name
            ORDER BY count(*) DESC
            LIMIT 10
            """, nativeQuery = true)
    List<LeaderboardMostPbsView> mostPbsLast7Days();

    @Cacheable("mostPbsLastMonth")
    @Query(value = """
            SELECT
                l.player_name as playerName,
                count(*) as entries,
                min(l.position) as bestPosition,
                round(avg(l.position)) as avgPosition
            FROM leaderboards l
                     LEFT JOIN tracks t ON t.id = l.track_id
            WHERE l.created_at > (now() - INTERVAL '1' MONTH)
              AND l.is_invalid_run = false
            GROUP BY l.player_name
            ORDER BY count(*) DESC
            LIMIT 10
            """, nativeQuery = true)
    List<LeaderboardMostPbsView> mostPbsLastMonth();

//    @Cacheable("worstTrackByPlayer")
    @Query(value = """
        WITH main as (SELECT t.id as track_id,
                             t.name as track_name,
                             t.map_name as track_map_name,
                             t.parent_category as track_parent_category,
                             l.id as player_id,
                             l.player_name as player_name,
                             l.position as player_position,
                             l.score as player_score,
                             l.created_at as player_created_at,
                             l.is_invalid_run as player_is_invalid_run
                      FROM tracks t
                        LEFT JOIN leaderboards l ON t.id = l.track_id AND l.player_name = :playerName
                      WHERE (:excludedTrackIds is null or t.id not in (:excludedTrackIds)))
        SELECT trackId,
               trackName,
               trackMapName,
               trackParentCategory,
               json_agg(reason ORDER BY reason) as reasons FROM (
        """+
// 10 last maps you didn't improve upon lately
        """
        
              (SELECT m.track_id               as trackId,
                      m.track_name             as trackName,
                      m.track_map_name         as trackMapName,
                      m.track_parent_category  as trackParentCategory,
                      'IMPROVEMENT_IS_LONG_AGO' as reason
               FROM main m
               WHERE m.player_is_invalid_run = false AND m.player_id is not null AND :includeImprovementIsLongAgo
               ORDER BY m.player_created_at
               LIMIT 10)
              UNION
        """+
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
                 AND m.player_id is not null AND :includeWorstPosition
               ORDER BY m.player_position DESC
               LIMIT 10)
              UNION
        """+
//  10 tracks with most beaten by entries and then order by oldest beaten by entry (because 5 is max)
        """
              (SELECT min(m.track_id)              as trackId,
                      min(m.track_name)            as trackName,
                      min(m.track_map_name)        as trackMapName,
                      min(m.track_parent_category) as trackParentCategory,
                      'MOST_BEATEN_BY_ENTRIES'     as reason
               FROM main m
                 INNER JOIN leaderboards_beaten_by b ON b.leaderboard_id = m.player_id
                 INNER JOIN leaderboards l ON l.id = b.beaten_by_leaderboard_id
               WHERE m.player_is_invalid_run = false and m.player_id is not null AND :includeMostBeatenByEntries
               GROUP BY m.player_id
               ORDER BY count(l.player_id) desc, min(l.created_at)
               limit 10)
              UNION
        """+
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
               WHERE m.player_id is not null AND :includeFarthestBehindLeader
               ORDER BY m.player_score - l.score DESC
               LIMIT 10)
              UNION
        """+
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
               WHERE m.player_id is not null AND :includePotentiallyEasyToAdvance
               ORDER BY (m.player_score - l.score) / m.player_position DESC
               LIMIT 10)
              UNION
        """+
// 10 tracks where your time is invalid
        """
              (SELECT m.track_id              as trackId,
                      m.track_name            as trackName,
                      m.track_map_name        as trackMapName,
                      m.track_parent_category as trackParentCategory,
                      'INVALID_RUN'           as reason
               FROM main m
               WHERE m.player_is_invalid_run = true AND m.player_id is not null AND :includeInvalidRuns
               ORDER BY m.player_position DESC
               LIMIT 10)
              UNION
        """+
// 10 tracks you didn't complete
        """
              (SELECT m.track_id              as trackId,
                      m.track_name            as trackName,
                      m.track_map_name        as trackMapName,
                      m.track_parent_category as trackParentCategory,
                      'NOT_COMPLETED'           as reason
               FROM main m
               WHERE m.player_id IS NULL AND :includeNotCompleted
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
            List<Long> excludedTrackIds);
}

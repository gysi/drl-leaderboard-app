package de.gregord.drlleaderboardbackend.repositories;

import de.gregord.drlleaderboardbackend.domain.CommunityRankingView;
import de.gregord.drlleaderboardbackend.domain.TrackCommunitySeasonView;
import de.gregord.drlleaderboardbackend.entities.CommunitySeason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommunitySeasonsRepository extends JpaRepository<CommunitySeason, CommunitySeason.CommunitySeasonId> {
    Optional<CommunitySeason> findByTrackId(Long trackId);

    boolean existsByTrackId(Long trackId);

    boolean existsByTrackIdAndExcludedIsFalse(Long trackId);

    @Query(value = """
                SELECT cs.track_id FROM community_seasons cs WHERE excluded = FALSE
            """, nativeQuery = true)
    List<Long> findTrackIdsByExcludedIsFalse();

    @Query(value = """
                SELECT cs.track_id FROM community_seasons cs WHERE cs.season_id = :seasonId AND excluded = FALSE
            """, nativeQuery = true)
    List<Long> findTrackIdsBySeasonIdAndExcludedIsFalse(int seasonId);

    List<CommunitySeason> findCommunitySeasonBySeasonIdAndExcludedIsFalse(int season);

    @Query("""
                SELECT cs FROM CommunitySeason cs JOIN FETCH cs.track t
                WHERE cs.seasonId = :seasonId and cs.excluded = FALSE
                ORDER BY t.name, COALESCE(cs.customTrackDifficulty, t.mapDifficulty)
            """)
    List<TrackCommunitySeasonView> findBySeasonIdAndExcludedIsFalseAndSortedByDifficulty(int seasonId);

    @Query(value = """
            WITH eligible_players AS
                     (SELECT DISTINCT p.id AS playerId
                      FROM (SELECT cs_i.track_id FROM community_seasons cs_i WHERE cs_i.season_id = :seasonId and excluded = FALSE) cs
                               INNER JOIN tracks t ON t.id = cs.track_id
                               INNER JOIN leaderboards l ON l.track_id = t.id
                               INNER JOIN players p ON p.id = l.player_id
                      WHERE l.created_at > :seasonStartDate),
                 overall_ranking AS
                     (SELECT l.player_id       AS playerId,
                             p.player_name      AS playerName,
                             sum(l.points)  AS totalPoints,
                             avg(l.position)     AS avgPosition,
                             min(l.position)     AS bestPosition,
                             count(*)       AS completedTracks,
                             sum(l.crash_count)   AS totalCrashCount,
                             max(l.top_speed)    AS maxTopSpeed,
                             sum(l.score) + coalesce(sum(l.time_penalty_total), 0) AS totalScore,
                             COALESCE(SUM(jsonb_array_length(penalties)), 0)  AS totalTimePenalty,
                             SUM(jsonb_array_length(penalties)) AS totalPenalties,
                             min(p.flag_url)     AS flagUrl,
                             min(p.profile_platform) AS profilePlatform,
                             min(p.profile_thumb)  AS profileThumb,
                             max(l.updated_at)    AS latestActivity,
                             CASE WHEN EXISTS(SELECT playerId from eligible_players ep where ep.playerId = l.player_id)
                                      THEN TRUE
                                  ELSE FALSE
                                 END AS isEligible
                      FROM (SELECT cs_i.track_id FROM community_seasons cs_i WHERE cs_i.season_id = :seasonId and excluded = FALSE) cs
                               INNER JOIN tracks t ON t.id = cs.track_id
                               INNER JOIN leaderboards l ON l.track_id = t.id
                               INNER JOIN players p ON p.id = l.player_id
                      WHERE is_invalid_run = FALSE
                      GROUP BY l.player_id, player_name
                      ORDER BY sum(points) DESC
                      LIMIT :limit OFFSET :offset),
                 invalid_runs AS (SELECT l.player_id,
                                         COALESCE(count(*), 0) as invalid_runs
                                  FROM (SELECT cs_i.track_id FROM community_seasons cs_i WHERE cs_i.season_id = :seasonId AND excluded = FALSE) cs
                                           INNER JOIN tracks t ON t.id = cs.track_id
                                           INNER JOIN leaderboards l on l.track_id = t.id
                                           INNER JOIN overall_ranking ovr ON l.player_id = ovr.playerId
                                  WHERE l.is_invalid_run = TRUE
                                  GROUP BY l.player_id)
            SELECT (CASE WHEN isEligible THEN 0 ELSE 1 END) +
                   (row_number() over (ORDER BY totalPoints DESC, totalScore)) -
                   SUM(CASE WHEN isEligible THEN 0 ELSE 1 END) OVER (ORDER BY totalPoints DESC, totalScore) as position,
                   playerId,
                   playerName,
                   totalPoints,
                   avgPosition,
                   bestPosition,
                   COALESCE((SELECT invalid_runs FROM invalid_runs ir WHERE ir.player_id = ovr.playerId), 0) AS invalidRuns,
                   completedTracks,
                   totalCrashCount,
                   totalScore,
                   totalTimePenalty,
                   totalPenalties,
                   maxTopSpeed,
                   substring(flagUrl FROM position('.png' IN flagUrl) - 2 FOR 2) AS flagUrl,
                   profilePlatform,
                   profileThumb,
                   latestActivity,
                   isEligible
            FROM overall_ranking ovr
            WHERE (:onlyEligible = FALSE OR isEligible = TRUE)
            """, nativeQuery = true)
    List<CommunityRankingView> getOverallRankingCurrentSeason(int seasonId, LocalDateTime seasonStartDate, boolean onlyEligible, int limit, int offset);

    @Modifying
    @Query(value = """
            INSERT INTO community_seasons (season_id, season_id_name, track_id, custom_track_difficulty, excluded)
            SELECT :seasonId, :seasonIdName, t.id,
                   CASE
                       WHEN t.name LIKE '%-01]%' THEN 0
                       WHEN t.name LIKE '%-02]%' OR t.name LIKE '%-03]%' OR t.name LIKE '%-04]%' OR t.name LIKE '%-05]%' THEN 1
                       WHEN t.name LIKE '%-06]%' OR t.name LIKE '%-07]%' OR t.name LIKE '%-08]%' OR t.name LIKE '%-09]%' OR t.name LIKE '%-10]%' THEN 1
                       WHEN t.name LIKE '%-11]%' OR t.name LIKE '%-12]%' OR t.name LIKE '%-13]%' OR t.name LIKE '%-14]%' OR t.name LIKE '%-15]%' THEN 2
                       WHEN t.name LIKE '%-16]%' OR t.name LIKE '%-17]%' OR t.name LIKE '%-18]%' OR t.name LIKE '%-19]%' OR t.name LIKE '%-20]%' THEN 3
                       WHEN t.name LIKE '%-21]%' OR t.name LIKE '%-22]%' OR t.name LIKE '%-23]%' OR t.name LIKE '%-24]%' OR t.name LIKE '%-25]%' THEN 3
                       END as custom_track_difficulty,
                   false
            FROM tracks t
            WHERE t.name ILIKE :seasonPrefix
            ON CONFLICT (season_id, track_id) DO NOTHING
            """, nativeQuery = true)
    int updateCustomCommunitySeasonTracks(Integer seasonId, String seasonIdName, String seasonPrefix);
}

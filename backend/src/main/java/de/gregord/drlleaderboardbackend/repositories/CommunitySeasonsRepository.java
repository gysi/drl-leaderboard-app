package de.gregord.drlleaderboardbackend.repositories;

import de.gregord.drlleaderboardbackend.domain.CommunityRankingView;
import de.gregord.drlleaderboardbackend.domain.TrackCommunitySeasonView;
import de.gregord.drlleaderboardbackend.entities.CommunitySeason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommunitySeasonsRepository extends JpaRepository<CommunitySeason, CommunitySeason.CommunitySeasonId> {
    Optional<CommunitySeason> findByTrackId(Long trackId);

    boolean existsByTrackId(Long trackId);

    @Query(value = """
                SELECT cs.track_id FROM community_seasons cs WHERE cs.season_id = :seasonId AND excluded = FALSE
            """, nativeQuery = true)
    List<Long> findTrackIdsBySeasonIdAndExcludedIsFalse(int seasonId);

    List<CommunitySeason> findCommunitySeasonBySeasonIdAndExcludedIsFalse(int season);

    @Query("""
                SELECT cs FROM CommunitySeason cs JOIN FETCH cs.track t
                WHERE cs.seasonId = :seasonId and cs.excluded = FALSE
                ORDER BY COALESCE(cs.customTrackDifficulty, t.mapDifficulty)
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
                             round(sum(l.points))  AS totalPoints,
                             avg(l.position)     AS avgPosition,
                             min(l.position)     AS bestPosition,
                             count(*)       AS completedTracks,
                             sum(l.crash_count)   AS totalCrashCount,
                             max(l.top_speed)    AS maxTopSpeed,
                             sum(l.score)      AS totalScore,
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
                   playerName,
                   totalPoints,
                   avgPosition,
                   bestPosition,
                   COALESCE((SELECT invalid_runs FROM invalid_runs ir WHERE ir.player_id = ovr.playerId), 0) AS invalidRuns,
                   completedTracks,
                   totalCrashCount,
                   totalScore,
                   maxTopSpeed,
                   substring(flagUrl FROM position('.png' IN flagUrl) - 2 FOR 2) AS flagUrl,
                   profilePlatform,
                   profileThumb,
                   latestActivity,
                   isEligible
            FROM overall_ranking ovr
                """, nativeQuery = true)
    List<CommunityRankingView> getOverallRankingCurrentSeason(int seasonId, LocalDateTime seasonStartDate, int limit, int offset);
}

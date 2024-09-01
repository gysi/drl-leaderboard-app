package de.gregord.drlleaderboardbackend.repositories;

import de.gregord.drlleaderboardbackend.domain.CommunityRankingHistoryView;
import de.gregord.drlleaderboardbackend.domain.CommunityRankingView;
import de.gregord.drlleaderboardbackend.entities.CommunitySeasonRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CommunitySeasonRankingHistoryRepository extends JpaRepository<CommunitySeasonRanking, Long> {

    @Query("select count(c) from CommunitySeasonRanking c where c.seasonId = ?1")
    long countBySeasonId(Long seasonId);

    @Query(value = """
            SELECT h.position position,
                   h.player_id playerId,
                   p.player_name playerName,
                   h.points totalPoints,
                   h.position_average avgPosition,
                   h.position_best bestPosition,
                   h.invalid_runs invalidRuns,
                   h.completed_tracks completedTracks,
                   h.crashes totalCrashCount,
                   h.total_time totalScore,
                   h.penalties totalPenalties,
                   h.top_speed maxTopSpeed,
                   substring(p.flag_url FROM position('.png' IN p.flag_url) - 2 FOR 2) AS flagUrl,
                   p.profile_platform profilePlatform,
                   p.profile_thumb profileThumb
            FROM community_seasons_ranking_history h
                INNER JOIN players p ON p.id = h.player_id
            WHERE h.season_id = :seasonId
            ORDER BY h.position
            LIMIT :limit OFFSET :offset
            """, nativeQuery = true)
    List<CommunityRankingHistoryView> getRanking(int seasonId, int limit, int offset);
}

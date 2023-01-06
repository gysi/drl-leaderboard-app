package de.gregord.drlleaderboardbackend.repositories;

import de.gregord.drlleaderboardbackend.domain.LeaderBoardByTrackView;
import de.gregord.drlleaderboardbackend.domain.LeaderboardByPlayerView;
import de.gregord.drlleaderboardbackend.domain.OverallRankingView;
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

    @Cacheable("overallranking")
    @Query(value = """
                WITH overall_ranking as (SELECT player_name        as playerName,
                                                round(sum(points)) as totalPoints,
                                                count(*)           as completedTracks,
                                                sum(crash_count)   as totalCrashCount,
                                                max(top_speed)     as maxTopSpeed,
                                                sum(score)         as totalScore
                                         FROM leaderboards l
                                         where is_invalid_run = false
                                         group by player_name
                                         order by sum(points) desc
                                         limit :limit OFFSET :offset),
                     invalid_runs AS (SELECT
                                          l.player_name,
                                          COALESCE(count(*), 0) as invalid_runs
                                      FROM leaderboards l left join overall_ranking ovr on l.player_name = ovr.playerName
                                      WHERE l.is_invalid_run = true
                                      GROUP BY l.player_name)
                SELECT ROW_NUMBER() OVER (ORDER BY totalPoints DESC) as position,
                       playerName,
                       totalPoints,
                       (select invalid_runs from invalid_runs ir where ir.player_name = ovr.playerName) as invalidRuns,
                       completedTracks,
                       totalCrashCount,
                       totalScore,
                       maxTopSpeed
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
}

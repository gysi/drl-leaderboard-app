package de.gregord.drlleaderboardbackend.repositories;

import de.gregord.drlleaderboardbackend.entities.CommunitySeasonRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommunitySeasonRankingHistoryRepository extends JpaRepository<CommunitySeasonRanking, Long> {

    @Query("select count(c) from CommunitySeasonRanking c where c.seasonId = ?1")
    long countBySeasonId(Long seasonId);
}

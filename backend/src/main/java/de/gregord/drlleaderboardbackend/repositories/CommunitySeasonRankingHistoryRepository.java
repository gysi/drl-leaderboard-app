package de.gregord.drlleaderboardbackend.repositories;

import de.gregord.drlleaderboardbackend.entities.CommunitySeasonRanking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface CommunitySeasonRankingHistoryRepository extends JpaRepository<CommunitySeasonRanking, BigInteger> {


}

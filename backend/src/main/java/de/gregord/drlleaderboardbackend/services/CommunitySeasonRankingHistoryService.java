package de.gregord.drlleaderboardbackend.services;

import de.gregord.drlleaderboardbackend.domain.CommunityRankingView;
import de.gregord.drlleaderboardbackend.domain.Season;
import de.gregord.drlleaderboardbackend.entities.CommunitySeasonRanking;
import de.gregord.drlleaderboardbackend.entities.Player;
import de.gregord.drlleaderboardbackend.repositories.CommunitySeasonRankingHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunitySeasonRankingHistoryService {
    private static final Logger LOG = LoggerFactory.getLogger(CommunitySeasonRankingHistoryService.class);

    private final CommunitySeasonRankingHistoryRepository communitySeasonRankingHistoryRepository;

    public CommunitySeasonRankingHistoryService(
            CommunitySeasonRankingHistoryRepository communitySeasonRankingHistoryRepository
    ) {
        this.communitySeasonRankingHistoryRepository = communitySeasonRankingHistoryRepository;
    }

    public void saveRankingHistory(Season season, List<CommunityRankingView> rankingHistory) {
        LOG.info("Saving community ranking history...");
        List<CommunitySeasonRanking> rankingToSave = rankingHistory.stream().map(communityRankingView -> {
            CommunitySeasonRanking communitySeasonRanking = new CommunitySeasonRanking();
            communitySeasonRanking.setSeasonId((long) season.getSeasonId());
            Player player = new Player();
            player.setId(communityRankingView.getPlayerId());
            communitySeasonRanking.setPlayer(player);
            communitySeasonRanking.setPoints(communityRankingView.getTotalPoints());
            communitySeasonRanking.setPosition(communityRankingView.getPosition());
            communitySeasonRanking.setPositionAverage(communityRankingView.getAvgPosition());
            communitySeasonRanking.setPositionBest(communityRankingView.getBestPosition());
            communitySeasonRanking.setInvalidRuns(communityRankingView.getInvalidRuns());
            communitySeasonRanking.setCompletedTracks(communityRankingView.getCompletedTracks());
            communitySeasonRanking.setCrashes(communityRankingView.getTotalCrashCount());
            communitySeasonRanking.setTotalTime(communityRankingView.getTotalScore());
            communitySeasonRanking.setTopSpeed(communityRankingView.getMaxTopSpeed());
            return communitySeasonRanking;
        }).toList();
        communitySeasonRankingHistoryRepository.saveAll(rankingToSave);
    }

    public long countBySeasonId(Long seasonId) {
        return communitySeasonRankingHistoryRepository.countBySeasonId(seasonId);
    }
}

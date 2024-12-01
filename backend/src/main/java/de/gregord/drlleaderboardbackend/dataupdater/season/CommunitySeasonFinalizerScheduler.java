package de.gregord.drlleaderboardbackend.dataupdater.season;

import de.gregord.drlleaderboardbackend.domain.CommunityRankingView;
import de.gregord.drlleaderboardbackend.domain.Season;
import de.gregord.drlleaderboardbackend.services.CommunitySeasonRankingHistoryService;
import de.gregord.drlleaderboardbackend.services.CommunitySeasonService;
import org.slf4j.Logger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@EnableScheduling
@Component
public class CommunitySeasonFinalizerScheduler {
    private static final Logger LOG = getLogger(CommunitySeasonFinalizerScheduler.class);

    private final CommunitySeasonService communitySeasonService;
    private final CommunitySeasonRankingHistoryService communitySeasonRankingHistoryService;

    public CommunitySeasonFinalizerScheduler(
            CommunitySeasonService communitySeasonService,
            CommunitySeasonRankingHistoryService communitySeasonRankingHistoryService
    ) {
        this.communitySeasonService = communitySeasonService;
        this.communitySeasonRankingHistoryService = communitySeasonRankingHistoryService;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(149)
    public void initialize() {
        finalizeQualifier();
        finalizeSeason();
    }

    @Scheduled(cron = "${app.data-updater.season.cron}")
    public void updateMapsData(){
        finalizeQualifier();
        finalizeSeason();
    }

    private void finalizeQualifier() {
        Season currentSeason = Season.getCurrentSeason();
        if (Season.NO_SEASON == currentSeason) {
            LOG.info("No current season found. Skipping qualifier finalization.");
            return;
        }

        if(currentSeason.getDetails_v1() == null){
            LOG.info("No details for current season found. Skipping qualifier finalization.");
            return;
        }

        if(currentSeason.getDetails_v1().qualificationEndDate == null){
            LOG.info("No qualification end date for current season found. Skipping qualifier finalization.");
            return;
        }

        if(currentSeason.getDetails_v1().qualificationEndDate.isAfter(LocalDateTime.now())){
            LOG.info("Qualifier has not ended yet. Skipping qualifier finalization.");
            return;
        }

        if(communitySeasonRankingHistoryService.countBySeasonId((long) currentSeason.getSeasonId()) > 0){
            LOG.info("Qualifier ranking history already exists for this season, Skipping qualifier finalization.");
            return;
        }

        List<CommunityRankingView> overallRankingCurrentSeasonNoCache =
                communitySeasonService.getOverallRankingForSeason(currentSeason,true,1, 100);
        communitySeasonRankingHistoryService.saveRankingHistory(currentSeason, overallRankingCurrentSeasonNoCache);
        LOG.info("Finalizing qualifier with {} entries", overallRankingCurrentSeasonNoCache.size());
    }

    private void finalizeSeason() {
        Season previousSeason = Season.getPreviousSeason();
        if (previousSeason == null) {
            LOG.warn("No previous season found. Skipping finalization.");
            return;
        }

        LocalDateTime seasonEndDate = previousSeason.getSeasonEndDate();
        if (seasonEndDate.isAfter(LocalDateTime.now())) {
            // normally this shouldn't really happen
            LOG.info("Previous season has not ended yet. Skipping finalization.");
            return;
        }

        if(communitySeasonRankingHistoryService.countBySeasonId((long) previousSeason.getSeasonId()) > 0){
            LOG.info("Season ranking history already exists for this season, Skipping finalization.");
            return;
        }

        List<CommunityRankingView> overallRankingCurrentSeasonNoCache =
                communitySeasonService.getOverallRankingForSeason(previousSeason,true,1, 100);
        communitySeasonRankingHistoryService.saveRankingHistory(previousSeason, overallRankingCurrentSeasonNoCache);
        LOG.info("Finalizing season with {} entries", overallRankingCurrentSeasonNoCache.size());
    }
}

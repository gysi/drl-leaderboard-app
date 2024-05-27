package de.gregord.drlleaderboardbackend.dataupdater;

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

import java.time.LocalDate;
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
        finalizeSeason();
    }

    @Scheduled(cron = "${app.data-updater.season.cron}")
    public void updateMapsData(){
        finalizeSeason();
    }

    private void finalizeSeason() {
        Season previousSeason = Season.getPreviousSeason();
        if (previousSeason == null) {
            LOG.warn("No previous season found. Skipping finalization.");
            return;
        }

        LocalDateTime seasonEndDate = previousSeason.getSeasonEndDate();
        if (seasonEndDate.toLocalDate().isBefore(LocalDate.now())) {
            LOG.warn("Previous season has not ended yet. Skipping finalization.");
            return;
        }

        List<CommunityRankingView> overallRankingCurrentSeasonNoCache =
                communitySeasonService.getOverallRankingForSeason(previousSeason,true,1, 100);
        communitySeasonRankingHistoryService.saveRankingHistory(previousSeason, overallRankingCurrentSeasonNoCache);
        LOG.info("Finalizing season with {} entries", overallRankingCurrentSeasonNoCache.size());
    }
}

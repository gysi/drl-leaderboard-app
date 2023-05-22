package de.gregord.drlleaderboardbackend.dataupdater;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class LeaderboardDataScheduler {

    LeaderboardUpdater leaderboardUpdater;

    public LeaderboardDataScheduler(LeaderboardUpdater tracksDataUpdater) {
        this.leaderboardUpdater = tracksDataUpdater;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(200)
    public void initialize() {
        leaderboardUpdater.initialize();
    }

    @Scheduled(cron = "${app.data-updater.leaderboards.cron}")
    public void updateLeaderboard(){
        leaderboardUpdater.updateLeaderboard();
    }
}

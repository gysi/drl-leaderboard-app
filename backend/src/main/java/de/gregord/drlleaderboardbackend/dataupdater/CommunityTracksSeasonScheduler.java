package de.gregord.drlleaderboardbackend.dataupdater;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class CommunityTracksSeasonScheduler {

    CommunityTracksSeasonUpdater communityTracksSeasonUpdater;

    public CommunityTracksSeasonScheduler(CommunityTracksSeasonUpdater communityTracksSeasonUpdater) {
        this.communityTracksSeasonUpdater = communityTracksSeasonUpdater;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(150)
    public void initialize() {
        communityTracksSeasonUpdater.updateCommunitySeasonTracks();
    }

    @Scheduled(cron = "${app.data-updater.season.cron}")
    public void updateMapsData(){
        communityTracksSeasonUpdater.updateCommunitySeasonTracks();
    }
}

package de.gregord.drlleaderboardbackend.dataupdater;

import org.springframework.beans.factory.annotation.Value;
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
    boolean isEnabled;

    public CommunityTracksSeasonScheduler(
            @Value("${app.data-updater.season.enabled}") boolean isEnabled,
            CommunityTracksSeasonUpdater communityTracksSeasonUpdater
    ) {
        this.isEnabled = isEnabled;
        this.communityTracksSeasonUpdater = communityTracksSeasonUpdater;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(150)
    public void initialize() {
        if(isEnabled) {
            communityTracksSeasonUpdater.updateCommunitySeasonTracks();
        }
    }

    @Scheduled(cron = "${app.data-updater.season.cron}")
    public void updateMapsData(){
        if(isEnabled) {
            communityTracksSeasonUpdater.updateCommunitySeasonTracks();
        }
    }
}

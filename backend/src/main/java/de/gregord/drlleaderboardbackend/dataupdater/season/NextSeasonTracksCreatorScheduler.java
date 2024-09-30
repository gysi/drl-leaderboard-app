package de.gregord.drlleaderboardbackend.dataupdater.season;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class NextSeasonTracksCreatorScheduler {

    NextSeasonTracksCreator nextSeasonTracksCreator;
    boolean isEnabled;

    public NextSeasonTracksCreatorScheduler(
            @Value("${app.data-updater.season.enabled}") boolean isEnabled,
            NextSeasonTracksCreator nextSeasonTracksCreator
    ) {
        this.isEnabled = isEnabled;
        this.nextSeasonTracksCreator = nextSeasonTracksCreator;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(150)
    public void initialize() {
        if(isEnabled) {
            nextSeasonTracksCreator.addSeasonTracks();
        }
    }

    @Scheduled(cron = "${app.data-updater.season.cron}")
    public void updateMapsData(){
        if(isEnabled) {
            nextSeasonTracksCreator.addSeasonTracks();
        }
    }
}

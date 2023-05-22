package de.gregord.drlleaderboardbackend.dataupdater;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class TracksDataScheduler {

    TracksDataUpdater tracksDataUpdater;

    public TracksDataScheduler(TracksDataUpdater tracksDataUpdater) {
        this.tracksDataUpdater = tracksDataUpdater;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(100)
    public void initialize() {
        tracksDataUpdater.initialize();
    }

    @Scheduled(cron = "${app.data-updater.tracks.cron}")
    public void updateMapsData(){
        tracksDataUpdater.updateMapsData();
    }
}

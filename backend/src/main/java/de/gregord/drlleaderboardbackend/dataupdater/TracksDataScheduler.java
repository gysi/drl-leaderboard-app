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

    TracksOfficialDataUpdater tracksOfficialDataUpdater;
    TracksCommunityDataUpdater tracksCommunityDataUpdater;

    public TracksDataScheduler(TracksOfficialDataUpdater tracksOfficialDataUpdater,
                               TracksCommunityDataUpdater tracksCommunityDataUpdater) {
        this.tracksOfficialDataUpdater = tracksOfficialDataUpdater;
        this.tracksCommunityDataUpdater = tracksCommunityDataUpdater;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(100)
    public void initialize() throws InterruptedException {
        var thread1 = Thread.ofVirtual().name("OfficialTracksDataUpdater").start(() -> tracksOfficialDataUpdater.initialize());
        var thread2 = Thread.ofVirtual().name("CommunityTracksDataUpdater").start(() -> tracksCommunityDataUpdater.initialize());
        thread1.join();
        thread2.join();
    }

    @Scheduled(cron = "${app.data-updater.tracks.cron}")
    public void updateMapsData() throws InterruptedException {
        var thread1 = Thread.ofVirtual().name("OfficialTracksDataUpdater").start(() -> tracksOfficialDataUpdater.updateMapsData());
        var thread2 = Thread.ofVirtual().name("CommunityTracksDataUpdater").start(() -> tracksCommunityDataUpdater.updateMapsData());
        thread1.join();
        thread2.join();
    }
}

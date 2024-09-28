package de.gregord.drlleaderboardbackend.dataupdater.leaderboard;

import de.gregord.drlleaderboardbackend.domain.Season;
import de.gregord.drlleaderboardbackend.repositories.TracksRepository;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

@EnableScheduling
@Component
public class LeaderboardDataScheduler {
    private static final Logger LOG = LoggerFactory.getLogger(LeaderboardDataScheduler.class);
    private final boolean isLeaderboardDataUpdaterEnabled;
    private final LeaderboardUpdater leaderboardUpdater;
    private final TracksRepository tracksRepository;
    private Thread thread1;
    private Thread thread2;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final ConcurrentLinkedDeque<Long> tracksProcessedNotSoLongAgo = new ConcurrentLinkedDeque<>();

    public LeaderboardDataScheduler(
            @Value("${app.data-updater.leaderboards.enabled}") boolean isLeaderboardDataUpdaterEnabled,
            LeaderboardUpdater tracksDataUpdater,
            TracksRepository tracksRepository) {
        this.isLeaderboardDataUpdaterEnabled = isLeaderboardDataUpdaterEnabled;
        this.leaderboardUpdater = tracksDataUpdater;
        this.tracksRepository = tracksRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(200)
    public void startLeaderboardUpdating() {
        if (isLeaderboardDataUpdaterEnabled) {
            Thread.Builder.OfVirtual threadBuilder = Thread.ofVirtual().name("leaderBoardUpdater", 1L);
            thread1 = startTrackThread(threadBuilder, this::getHighestPriorityTracks);
            thread2 = startTrackThread(threadBuilder, this::getLowestPriorityTracks);
        }
    }

    public List<Long> getTracksOrderedByPriority() {
        return tracksRepository.getTracksToBeUpdatedByPriority(Season.getCurrentSeasonId());
    }

    public List<Long> getHighestPriorityTracks() {
        List<Long> tracksOrderedByPriority = getTracksOrderedByPriority();
        int totalTracks = tracksOrderedByPriority.size();
        int endIndex = (int) (totalTracks * 0.15);
        endIndex = Math.min(endIndex, totalTracks);
        return tracksOrderedByPriority.subList(0, endIndex);
    }

    public List<Long> getLowestPriorityTracks() {
        List<Long> tracksOrderedByPriority = getTracksOrderedByPriority();
        int totalTracks = tracksOrderedByPriority.size();
        int startIndex = (int) (totalTracks * 0.15);
        startIndex = Math.min(startIndex, totalTracks);
        startIndex = Math.max(startIndex, 0);
        return tracksOrderedByPriority.subList(startIndex, totalTracks);
    }

    public Thread startTrackThread(Thread.Builder threadBuilder, Supplier<List<Long>> trackSupplier) {
        return threadBuilder.start(() -> {
            LOG.info("Starting Thread to process tracks... {}", Thread.currentThread().getName());
            while (running.get()) {
                List<Long> tracksToProcess = trackSupplier.get();
                for (Long trackId : tracksToProcess) {
                    if (tracksProcessedNotSoLongAgo.contains(trackId)) {
                        LOG.info("Track {} was processed recently, skipping...", trackId);
                        continue;
                    }
                    tracksProcessedNotSoLongAgo.add(trackId);
                    if (tracksProcessedNotSoLongAgo.size() > 5) {
                        tracksProcessedNotSoLongAgo.poll();
                    }
                    tracksRepository.findById(trackId).ifPresent(leaderboardUpdater::updateLeaderboardForTrack);
                    if(Boolean.FALSE.equals(running.get())){
                        break;
                    }
                }
            }
        });
    }

    @PreDestroy
    public void stopLeaderboardUpdating() throws InterruptedException {
        running.set(false);
        if (thread1 != null) {
            thread1.join();
        }
        if (thread2 != null) {
            thread2.join();
        }
    }
}

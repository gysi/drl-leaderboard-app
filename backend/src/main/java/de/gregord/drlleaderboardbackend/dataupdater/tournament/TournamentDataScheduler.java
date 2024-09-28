package de.gregord.drlleaderboardbackend.dataupdater.tournament;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class TournamentDataScheduler {

    TournamentDataUpdater tournamentDataUpdater;

    public TournamentDataScheduler(TournamentDataUpdater tournamentDataUpdater) {
        this.tournamentDataUpdater = tournamentDataUpdater;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(300)
    public void initialize() {
        tournamentDataUpdater.initialize();
    }

    @Scheduled(cron = "${app.data-updater.tournaments.cron}")
    public void updateData(){
        tournamentDataUpdater.updateData();
    }
}

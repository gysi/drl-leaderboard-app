package de.gregord.drlleaderboardbackend.dataupdater;

import de.gregord.drlleaderboardbackend.repositories.TournamentRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

@Component
public class TournamentDataUpdater {
    public static Logger LOG = LoggerFactory.getLogger(TournamentDataUpdater.class);

    private final String token;
    private final String tournamentEndpoint;
    private final String tournamentDetailEndpoint;
    private final TournamentRepository tournamentRepository;

    public TournamentDataUpdater(
            @Value("${app.drl-api.token}") String token,
            @Value("${app.drl-api.tournament-endpoint}") String tournamentEndpoint,
            @Value("${app.drl-api.tournament-detail-endpoint}") String tournamentDetailEndpoint,
            TournamentRepository tournamentRepository
    ){
        this.token = token;
        this.tournamentEndpoint = tournamentEndpoint;
        this.tournamentDetailEndpoint = tournamentDetailEndpoint;
        this.tournamentRepository = tournamentRepository;
    }

    @Transactional
    @CacheEvict(value = "tournaments", allEntries = true)
    public void initialize() {
        long count = tournamentRepository.count();
        if(count <= 0) {
            LOG.info("No tournaments found in database, initializing...");
            updateData();
        }
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "tournaments", allEntries = true),
    })
    public void updateData() {

    }
}

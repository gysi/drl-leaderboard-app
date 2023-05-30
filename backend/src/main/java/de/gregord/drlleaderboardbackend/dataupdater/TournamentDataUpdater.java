package de.gregord.drlleaderboardbackend.dataupdater;

import de.gregord.drlleaderboardbackend.entities.Tournament;
import de.gregord.drlleaderboardbackend.entities.tournament.TournamentRanking;
import de.gregord.drlleaderboardbackend.entities.tournament.TournamentRound;
import de.gregord.drlleaderboardbackend.repositories.TournamentRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class TournamentDataUpdater {
    public static Logger LOG = LoggerFactory.getLogger(TournamentDataUpdater.class);

    private final String token;
    private final String tournamentEndpoint;
    private final String tournamentDetailEndpoint;
    private final TournamentRepository tournamentRepository;
    private final CacheManager cacheManager;

    public TournamentDataUpdater(
            @Value("${app.drl-api.token}") String token,
            @Value("${app.drl-api.tournament-endpoint}") String tournamentEndpoint,
            @Value("${app.drl-api.tournament-detail-endpoint}") String tournamentDetailEndpoint,
            TournamentRepository tournamentRepository,
            CacheManager cacheManager
    ){
        this.token = token;
        this.tournamentEndpoint = tournamentEndpoint;
        this.tournamentDetailEndpoint = tournamentDetailEndpoint;
        this.tournamentRepository = tournamentRepository;
        this.cacheManager = cacheManager;
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
        LOG.info("Updating tournament data...");
        UriComponentsBuilder tournamentEndpointBuilder = UriComponentsBuilder.fromUriString(tournamentEndpoint);
        String requestUrl = tournamentEndpointBuilder.buildAndExpand(Map.of("token", token )).toUriString();
        try {
            ResponseEntity<Map> exchange = (new RestTemplate()).exchange(requestUrl, HttpMethod.GET, null, Map.class);
            Map<String, Object> body = exchange.getBody();
            if (body == null) {
                LOG.error("Could not retrieve tournament data from DRL API");
                return;
            }
            List<Map<String, Object>> data = (List<Map<String, Object>>) body.get("data");
            if (data == null) {
                LOG.error("Could not retrieve tournament data from DRL API");
                return;
            }
            for (Map<String, Object> tournamentJSON : data) {
                String tournamentDrlId = (String) tournamentJSON.get("id");
                String tournamentGuid = (String) tournamentJSON.get("guid");
                Tournament tournament = tournamentRepository.findTournamentByDrlId(tournamentDrlId).orElseGet(Tournament::new);
                Boolean tournamentAlreadyExists = tournament.getId() != null;
                if(tournamentAlreadyExists && tournament.getStatus().equals("completed")){
                    LOG.info("Tournament {} is already completed and saved within the db, skipping...", tournamentGuid);
                    continue;
                }
                UriComponentsBuilder tournamentDetailEndpointBuilder = UriComponentsBuilder.fromUriString(tournamentDetailEndpoint);
                String requestUrlDetail = tournamentDetailEndpointBuilder.buildAndExpand(Map.of("token", token, "guid", tournamentGuid)).toUriString();
                ResponseEntity<Map> exchangeDetail = (new RestTemplate()).exchange(requestUrlDetail, HttpMethod.GET, null, Map.class);
                Map<String, Object> bodyDetail = exchangeDetail.getBody();
                if (bodyDetail == null) {
                    LOG.error("Could not retrieve tournamentDetail from DRL API. guid: {}", tournamentGuid);
                    continue;
                }
                Map<String, Object> tournamentDetailJSON = ((List<Map<String, Object>>) bodyDetail.get("data")).get(0);
                tournament.setStatus((String) tournamentJSON.get("status"));
                tournament.setGuid(tournamentGuid);
                tournament.setDrlId(tournamentDrlId);
                tournament.setTitle((String) tournamentJSON.get("title"));
                if(tournament.getTitle().toLowerCase().contains("test")
                        || tournament.getGuid().toLowerCase().contains("test")){
                    tournament.setIsTestTournament(true);
                } else {
                    tournament.setIsTestTournament(false);
                }
                tournament.setDescription((String) tournamentJSON.get("description"));
                tournament.setImageUrl((String) tournamentJSON.get("image-url"));
                tournament.setRegion((String) tournamentJSON.get("region"));
                tournament.setIsPrivate((Boolean) tournamentJSON.get("private"));
                tournament.setPlayersSize((Integer) tournamentDetailJSON.get("players-size"));
                tournament.setPlayersPerMatch((String) tournamentDetailJSON.get("players-per-match"));
                tournament.setWinnersPerMatch((String) tournamentDetailJSON.get("winners-per-match"));
                tournament.setHeatsPerMatch((String) tournamentDetailJSON.get("heats-per-match"));
                tournament.setCreatedAt(LocalDateTime.from(ZonedDateTime.parse((String) tournamentJSON.get("created-at"))));
                tournament.setUpdatedAt(LocalDateTime.from(ZonedDateTime.parse((String) tournamentJSON.get("updated-at"))));
                tournament.setRegistrationStartAt(LocalDateTime.from(ZonedDateTime.parse((String) tournamentJSON.get("register-start"))));
                tournament.setRegistrationEndAt(LocalDateTime.from(ZonedDateTime.parse((String) tournamentJSON.get("register-end"))));
                tournament.setNextTurnAt(
                        tournamentJSON.get("next-turn") == null ? null :
                        LocalDateTime.from(ZonedDateTime.parse((String) tournamentJSON.get("next-turn")))
                );
                tournament.setCompletedAt(
                        tournamentJSON.get("completed-at") == null ? null :
                        LocalDateTime.from(ZonedDateTime.parse((String) tournamentJSON.get("completed-at")))
                );

                tournament.setPlayerIds((List<String>) tournamentDetailJSON.get("player-ids"));

                List<Map<String, Object>> rankingsJSON = (List<Map<String, Object>>) tournamentDetailJSON.get("ranking");
                List<TournamentRanking> tournamentRankings = new ArrayList<>();
                for (Map<String, Object> rankingJSON : rankingsJSON) {
                    TournamentRanking tournamentRanking = new TournamentRanking();
                    tournamentRanking.setPlayerId((String) rankingJSON.get("player-id"));
                    tournamentRanking.setSteamId((String) rankingJSON.get("steam-id"));
                    tournamentRanking.setProfileName((String) rankingJSON.get("profile-name"));
                    tournamentRanking.setProfileColor((String) rankingJSON.get("profile-color"));
                    tournamentRanking.setProfileThumb((String) rankingJSON.get("profile-thumb"));
                    tournamentRanking.setFlagUrl((String) rankingJSON.get("flag-url"));
                    tournamentRanking.setPlatform((String) rankingJSON.get("platform"));
                    tournamentRanking.setScore(
                            rankingJSON.get("score") == null ? null :
                            ((Integer) rankingJSON.get("score")).longValue()
                    );
                    tournamentRanking.setScoreTotal(
                            rankingJSON.get("score_total") == null ? null :
                            ((Integer) rankingJSON.get("score_total")).longValue()
                    );
                    tournamentRanking.setOrderScoreTotal(
                            rankingJSON.get("order_score_total") == null ? null :
                            ((Integer) rankingJSON.get("order_score_total")).longValue()
                    );
                    tournamentRanking.setGoldenPos((Integer) rankingJSON.get("golden_pos"));
                    tournamentRankings.add(tournamentRanking);
                }
                tournament.setRankings(tournamentRankings);

                List<Map<String, Object>> roundsJSON = (List<Map<String, Object>>) tournamentDetailJSON.get("rounds");
                List<TournamentRound> tournamentRounds = new ArrayList<>();
                boolean isMapSet = false;
                for (Map<String, Object> roundJSON : roundsJSON) {
                    if(!isMapSet){
                        tournament.setMap((String) roundJSON.get("map"));
                        tournament.setCustomMap((String) roundJSON.get("custom-map"));
                        tournament.setCustomMapTitle((String) roundJSON.get("custom-map-title"));
                        tournament.setTrack((String) roundJSON.get("track"));
                        isMapSet = true;
                    }
                    TournamentRound tournamentRound = new TournamentRound();
                    tournamentRound.setId((String) roundJSON.get("id"));
                    tournamentRound.setNOrder((Integer) roundJSON.get("norder"));
                    tournamentRound.setTitle((String) roundJSON.get("title"));
                    tournamentRound.setStatus((String) roundJSON.get("status"));
                    tournamentRound.setMode((String) roundJSON.get("mode"));
                    tournamentRound.setStartAt(LocalDateTime.from(ZonedDateTime.parse((String) roundJSON.get("start-at"))));
                    tournamentRound.setEndAt(LocalDateTime.from(ZonedDateTime.parse((String) roundJSON.get("end-at"))));
                    tournamentRounds.add(tournamentRound);
                    List<Map<String, Object>> matchesJSON = (List<Map<String, Object>>) roundJSON.get("matches");
                    List<TournamentRound.Match> tournamentMatches = new ArrayList<>();
                    for (Map<String, Object> matchJSON : matchesJSON) {
                        TournamentRound.Match tournamentMatch = new TournamentRound.Match();
                        tournamentMatch.setId((String) matchJSON.get("id"));
                        tournamentMatch.setStatus((String) matchJSON.get("status"));
                        tournamentMatch.setNOrder((Integer) matchJSON.get("norder"));
                        tournamentMatch.setNumWinners((Integer) matchJSON.get("num-winners"));
                        tournamentMatch.setHeats((Integer) matchJSON.get("heats"));
                        tournamentMatch.setActiveHeat((Integer) matchJSON.get("active-heat"));
                        tournamentMatch.setCurrentHeat((Integer) matchJSON.get("current-heat"));
                        tournamentMatch.setPlayersSize((Integer) matchJSON.get("players-size"));
                        tournamentMatch.setPlayerOrder((List<String>) matchJSON.get("player-order"));
                        List<Map<String, Object>> playersJSON = (List<Map<String, Object>>) matchJSON.get("players");
                        List<TournamentRound.Player> players = new ArrayList<>();
                        for (Map<String, Object> playerJSON : playersJSON) {
                            TournamentRound.Player player = new TournamentRound.Player();
                            player.setPlayerId((String) playerJSON.get("player-id"));
                            player.setSteamId((String) playerJSON.get("steam-id"));
                            player.setProfileName((String) playerJSON.get("profile-name"));
                            player.setProfileColor((String) playerJSON.get("profile-color"));
                            player.setProfileThumb((String) playerJSON.get("profile-thumb"));
                            player.setFlagUrl((String) playerJSON.get("flag-url"));
                            player.setPlatform((String) playerJSON.get("platform"));
                            players.add(player);
                        }
                        tournamentMatch.setPlayers(players);
                        tournamentMatches.add(tournamentMatch);
                    }
                    tournamentRound.setMatches(tournamentMatches);
                }
                tournament.setRounds(tournamentRounds);
                LOG.info("tournament: " + tournament);
                tournamentRepository.save(tournament);
                Optional.ofNullable(cacheManager.getCache("tournamentRankings"))
                        .ifPresent(Cache::invalidate);
            }
            LOG.info("requestUrl: " + requestUrl);
        } catch (Exception e) {
            LOG.error("Could not retrieve tournament data from DRL API", e);
        }
    }
}

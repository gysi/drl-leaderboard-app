package de.gregord.drlleaderboardbackend.services;

import de.gregord.drlleaderboardbackend.domain.InvalidRunReasons;
import de.gregord.drlleaderboardbackend.domain.PointsCalculation;
import de.gregord.drlleaderboardbackend.entities.LeaderboardEntry;
import de.gregord.drlleaderboardbackend.entities.Player;
import de.gregord.drlleaderboardbackend.entities.Track;
import de.gregord.drlleaderboardbackend.entities.TrackMinimal;
import de.gregord.drlleaderboardbackend.repositories.LeaderboardRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class DRLApiService {
    public static final Logger LOG = LoggerFactory.getLogger(DRLApiService.class);
    private final String token;
    private final String mapDetailEndpoint;
    private final String leaderboardEndpoint;
    private final PlayerService playerService;
    private final TracksService tracksService;
    private final LeaderboardRepository leaderboardRepository;
    private final ModelMapper modelMapper;
    private final Duration durationBetweenRequests;
    private Long waitMillisToResetRequestLimit = null;
    private LocalDateTime lastApiRequest = LocalDateTime.now();
    private final Object apiWaitLock = new Object();
    private static final Map<String, Double> customTopSpeeds = new HashMap<>();
    // <player_id, player_id>
    private static final Map<String, List<String>> doubleAccountMatchingMap = new HashMap<>();
    private static final Map<String, InvalidRunReasons> manualInvalidRuns = new HashMap<>();

    static {
        customTopSpeeds.put("MT-9eb", 104.6);
        customTopSpeeds.put("CMP-7ab877a2c62446a10c9316a0", 104.2);
        customTopSpeeds.put("MT-fb1", 102.0); // Straight line

        // pawelos -> p
        doubleAccountMatchingMap.put("6376865daa4f489bfc97a2a6", List.of("5a47746373f050000fb2e47d"));
        doubleAccountMatchingMap.put("5a47746373f050000fb2e47d", List.of("6376865daa4f489bfc97a2a6"));
        // srgmajordangle
        doubleAccountMatchingMap.put("609231f81903f7802cf35459", List.of("622d49e1a542747a374699e3"));
        doubleAccountMatchingMap.put("622d49e1a542747a374699e3", List.of("609231f81903f7802cf35459"));
        // syl
        doubleAccountMatchingMap.put("628e51b31a9d7664deaa3cf3", List.of("5c78aebaba3bdc580efdcaa0"));
        doubleAccountMatchingMap.put("5c78aebaba3bdc580efdcaa0", List.of("628e51b31a9d7664deaa3cf3"));
        // cakefpv
        doubleAccountMatchingMap.put("5d5d99a7b6b937c08cbb5443", List.of("6347b22828c01d57b57a62f1"));
        doubleAccountMatchingMap.put("6347b22828c01d57b57a62f1", List.of("5d5d99a7b6b937c08cbb5443"));
        // mckfpv
        doubleAccountMatchingMap.put("5e3969116ef085dae1fd1247", List.of("6387411d6bb427f37cc73253", "64f32b0c91cf4c08c243cf55"));
        doubleAccountMatchingMap.put("6387411d6bb427f37cc73253", List.of("5e3969116ef085dae1fd1247", "64f32b0c91cf4c08c243cf55"));
        doubleAccountMatchingMap.put("64f32b0c91cf4c08c243cf55", List.of("5e3969116ef085dae1fd1247", "6387411d6bb427f37cc73253"));

        // halowalker
        doubleAccountMatchingMap.put("5d9c2a106ef085dae105f1e2", List.of("6373e60eaa4f489bfc306d3d"));
        doubleAccountMatchingMap.put("6373e60eaa4f489bfc306d3d", List.of("5d9c2a106ef085dae105f1e2"));
        // killian
        doubleAccountMatchingMap.put("637d4834b060342b17397788", List.of("5988cd58d229c2460761a234"));
        doubleAccountMatchingMap.put("5988cd58d229c2460761a234", List.of("637d4834b060342b17397788"));
        // alexfpv
        doubleAccountMatchingMap.put("5a0f173ecd270d000fb02b5d", List.of("6375d300aa4f489bfcf8f9db"));
        doubleAccountMatchingMap.put("6375d300aa4f489bfcf8f9db", List.of("5a0f173ecd270d000fb02b5d"));
        // animositee
        doubleAccountMatchingMap.put("638d60f86bb427f37c0c212b", List.of("59c3b379d229c2460761c484"));
        doubleAccountMatchingMap.put("59c3b379d229c2460761c484", List.of("638d60f86bb427f37c0c212b"));
        // im back :)
        doubleAccountMatchingMap.put("5a0cecc2bad69d0014b7a8fe", List.of("634462ec28c01d57b50bee92"));
        doubleAccountMatchingMap.put("634462ec28c01d57b50bee92", List.of("5a0cecc2bad69d0014b7a8fe"));

        // cloneno1 Moonlight #1
        manualInvalidRuns.put("62a2459bd5155d003a982cd9", InvalidRunReasons.NO_REPLAY);
        // emzies2010 Straight Line #1
        manualInvalidRuns.put("649dee068116aa000a676b8f", InvalidRunReasons.WRONG_DRONE);
        // hsufoundation5 Straight Line #2
        manualInvalidRuns.put("64af0b982e81e5002839799a", InvalidRunReasons.WRONG_DRONE);
        // filthy radish Straight Line #1
        manualInvalidRuns.put("655c78c6f5f438000a237dae", InvalidRunReasons.WRONG_DRONE);
        // mckfpv Atlanta #2
        manualInvalidRuns.put("63a16b5e667fbd0022521f9f", InvalidRunReasons.NO_REPLAY);
        // MadOhmz SILENT #FREETHEBRIDGE
        manualInvalidRuns.put("6610c47974116e0011b589f2", InvalidRunReasons.WRONG_REPLAY);
        // curtiss_66 DRL 2022-23 METACITY
        manualInvalidRuns.put("6629cffeaaf498000a576b69", InvalidRunReasons.WRONG_REPLAY);
    }

    public DRLApiService(
            @Value("${app.drl-api.token}") String token,
            @Value("${app.drl-api.map-detail-endpoint}") String mapDetailEndpoint,
            @Value("${app.drl-api.leaderboard-endpoint}") String leaderboardEndpoint,
            @Value("${app.drl-api.duration-between-requests}") Duration durationBetweenRequests,
            PlayerService playerService,
            TracksService tracksService,
            LeaderboardRepository leaderboardRepository,
            ModelMapper modelMapper
    ) {
        this.token = token;
        this.mapDetailEndpoint = mapDetailEndpoint;
        this.leaderboardEndpoint = leaderboardEndpoint;
        this.durationBetweenRequests = durationBetweenRequests;
        this.playerService = playerService;
        this.tracksService = tracksService;
        this.leaderboardRepository = leaderboardRepository;
        this.modelMapper = modelMapper;

    }

    public <T> ResponseEntity<T> waitForApiLimitAndExecuteRequest(Supplier<ResponseEntity<T>> requestExecutor) throws RestClientException {
        synchronized (apiWaitLock) {
            do {
                LocalDateTime now = LocalDateTime.now();
                long millisBetweenRequest = durationBetweenRequests.toMillis();
                long millisPassedSinceLastRequest =
                        Duration.between(
                                lastApiRequest.atZone(ZoneId.systemDefault()).toInstant(),
                                now.atZone(ZoneId.systemDefault()).toInstant()
                        ).toMillis();

                if(waitMillisToResetRequestLimit != null){
                    try {
                        LOG.info("Waiting for api rate limit to reset {}ms", waitMillisToResetRequestLimit);
                        Thread.sleep(waitMillisToResetRequestLimit);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        LOG.error("Thread was interrupted while waiting to respect API limit", e);
                    }
                    waitMillisToResetRequestLimit = null;
                } else if (millisPassedSinceLastRequest < millisBetweenRequest) {
                    try {
                        Thread.sleep(millisBetweenRequest - millisPassedSinceLastRequest);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        LOG.error("Thread was interrupted while waiting to respect API limit", e);
                    }
                }

                try {
                    lastApiRequest = LocalDateTime.now();
                    ResponseEntity<T> response = requestExecutor.get();
                    HttpHeaders headers = response.getHeaders();
                    String remaining = headers.getFirst("x-ratelimit-remaining");
                    String reset = headers.getFirst("x-ratelimit-reset");
                    if (remaining != null && reset != null) {
                        long remainingRequests = Integer.parseInt(remaining);
                        LOG.debug("Remaining api requests: {}", remainingRequests);
                        long resetEpochSecond = Long.parseLong(reset);
                        if(remainingRequests <= 30){
                            LOG.info("Remaining api requests are <=30 ({}), set a wait time for the next request", remainingRequests);
                            long millisToWait = (resetEpochSecond * 1000L) - System.currentTimeMillis();
                            if(millisToWait > millisBetweenRequest) {
                                waitMillisToResetRequestLimit = (resetEpochSecond * 1000L) - System.currentTimeMillis();
                            }
                        }
                    }
                    return response;
                } catch (HttpClientErrorException.TooManyRequests tooManyRequestsError) {
                    LOG.warn("Too many requests, this shouldn't really happen anymore", tooManyRequestsError);
                }
            } while (true);
        }
    }

    public LeaderboardProcessorResult getAndProcessLeaderboardEntries(
            Track track,
            int pageLimit,
            int maxEntriesToProcess,
            LeaderboardProcessor leaderboardProcessor
    ) throws Exception {
        final LeaderboardProcessorResult leaderboardProcessorResult = new LeaderboardProcessorResult();

        // <PlayerId, LeaderboardDto>
        Map<String, LeaderboardEntry> currentLeaderboardEntries = new HashMap<>();
        if (track.getId() != null) {
            leaderboardRepository.findByTrack(modelMapper.map(track, TrackMinimal.class)).forEach(leaderboard -> currentLeaderboardEntries.put(leaderboard.getPlayer().getPlayerId(), leaderboard));
        }
        boolean isNewTrack = currentLeaderboardEntries.isEmpty();
        UriComponentsBuilder mapsEndpointBuilder = UriComponentsBuilder.fromUriString(leaderboardEndpoint);
        if (track.getGuid().startsWith("CMP")) {
            mapsEndpointBuilder
                    .queryParam("custom-map", track.getGuid())
                    .queryParam("is-custom-map", "true");
        } else {
            mapsEndpointBuilder
                    .queryParam("track", track.getGuid())
                    .queryParam("map", track.getMapId())
                    .queryParam("is-custom-map", "false");
        }
        int page = 1;
        String nextPageUrl;
        long leaderboardPosition = 1;
        // <player_id, leaderboard>
        Map<String, LeaderboardEntry> alreadyFoundPlayerIds = new HashMap<>();
        // <player_name, leaderboard>
        Map<String, LeaderboardEntry> alreadyFoundPlayerNames = new HashMap<>();
        Long leaderScore = null;
        do {
            String requestUrl = mapsEndpointBuilder.buildAndExpand(Map.of("token", token, "page", page, "limit", pageLimit)).toUriString();
            LOG.info("requestUrl: {}", requestUrl);
            ResponseEntity<Map<String, Object>> exchange =
                    waitForApiLimitAndExecuteRequest(() -> (new RestTemplate()).exchange(requestUrl, HttpMethod.GET, null,
                            new ParameterizedTypeReference<>() {
                            }));
            String content_length = Optional.ofNullable(exchange.getHeaders().get("Content-Length")).map(List::getFirst).orElse("0");
            leaderboardProcessorResult.setResponseContentLength(Long.parseLong(content_length.trim()));
            Map<String, Object> response = exchange.getBody();

            if (response == null || response.get("success") == null || !(boolean) response.get("success")) {
                LOG.error("Error while fetching leaderboard for track " + track.getName() + " with map id " + track.getMapId() + " and " +
                        "track id " + track.getDrlTrackId() + " on page " + page);
                throw new Exception();
            }
            @SuppressWarnings("unchecked") Map<String, Object> data = (Map<String, Object>) response.get("data");
            @SuppressWarnings("unchecked") Map<String, Object> paging = (Map<String, Object>) data.get("pagging");
            @SuppressWarnings("unchecked") List<Map<String, Object>> leaderboard = (List<Map<String, Object>>) data.get("leaderboard");

            if (leaderboard.isEmpty()) {
                LOG.warn("No leaderboard entries found for track " + track.getName() + " with map id " + track.getMapId() + " and track " +
                        "id " + track.getDrlTrackId() + " on page " + page);
            }

            final List<String> playerIdsFromApiPage = leaderboard.stream().map(entry -> (String) entry.get("player-id")).toList();
            final Map<String, Player> existingPlayersForDrlPlayerIdsOfTrack =
                    getExistingPlayersForDrlPlayerIdsOfTrack(currentLeaderboardEntries, playerIdsFromApiPage);

            for (Map<String, Object> drlLeaderboardEntry : leaderboard) {
                LeaderboardEntry leaderboardEntry;
                Optional<LeaderboardEntry> existingEntryOpt =
                        Optional.ofNullable(currentLeaderboardEntries.get((String) drlLeaderboardEntry.get("player-id")));
                leaderboardEntry = existingEntryOpt.orElseGet(LeaderboardEntry::new);
                Player playerForEntry;
                Player existingPlayer = null;
                LeaderboardEntry existingEntry = null;
                if (existingEntryOpt.isPresent()) {
                    playerForEntry = leaderboardEntry.getPlayer();
                    existingEntry = LeaderboardEntry.simpleCopy(existingEntryOpt.get());
                } else {
                    existingPlayer = existingPlayersForDrlPlayerIdsOfTrack.get((String) drlLeaderboardEntry.get("player-id"));
                    playerForEntry = Optional.ofNullable(existingPlayer).map(Player::simpleCopy).orElseGet(Player::new);
                    leaderboardEntry.setPlayer(playerForEntry);
                }
                playerForEntry.setPlayerId((String) drlLeaderboardEntry.get("player-id"));
                if (alreadyFoundPlayerIds.containsKey(playerForEntry.getPlayerId())) {
                    LOG.warn("Player " + playerForEntry.getPlayerId() + " already exists in this leaderboard, DRL BUG!");
                    continue;
                }
                playerForEntry.setPlayerName((String) drlLeaderboardEntry.get("username"));
                playerForEntry.setFlagUrl((String) drlLeaderboardEntry.get("flag-url"));
                playerForEntry.setProfilePlatform((String) drlLeaderboardEntry.get("profile-platform"));
                playerForEntry.setProfilePlatformId((String) drlLeaderboardEntry.get("profile-platform-id"));
                playerForEntry.setProfileThumb((String) drlLeaderboardEntry.get("profile-thumb"));
                leaderboardEntry.setDrlId((String) drlLeaderboardEntry.get("id"));
                leaderboardEntry.setTrack(modelMapper.map(track, TrackMinimal.class));
                leaderboardEntry.setPlayerIdDrl((String) drlLeaderboardEntry.get("player-id"));
                leaderboardEntry.setCrashCount((Integer) drlLeaderboardEntry.get("crash-count"));
                leaderboardEntry.setScore(Long.valueOf((Integer) drlLeaderboardEntry.get("score")));
                leaderboardEntry.setDroneName((String) drlLeaderboardEntry.get("drone-name"));
                leaderboardEntry.setTopSpeed(((Number) drlLeaderboardEntry.get("top-speed")).doubleValue());
                leaderboardEntry.setReplayUrl((String) drlLeaderboardEntry.get("replay-url"));
                leaderboardEntry.setCreatedAt(LocalDateTime.from(ZonedDateTime.parse((String) drlLeaderboardEntry.get("created-at"))));
                leaderboardEntry.setUpdatedAt(LocalDateTime.from(ZonedDateTime.parse((String) drlLeaderboardEntry.get("updated-at"))));
                leaderboardEntry.setInvalidRunReason(null);
                leaderboardEntry.setIsInvalidRun(false);
                if (alreadyFoundPlayerNames.containsKey(playerForEntry.getPlayerName())) {
                    LeaderboardEntry alreadyExistingEntry = alreadyFoundPlayerNames.get(playerForEntry.getPlayerName());
                    if (Boolean.FALSE.equals(alreadyExistingEntry.getIsInvalidRun())) {
                        LOG.warn("Playername " + playerForEntry.getPlayerName() + " (" + playerForEntry.getPlayerId() + ") already exists" +
                                " in this leaderboard (id: " + alreadyExistingEntry.getId() + ")");
                        leaderboardEntry.setIsInvalidRun(true);
                        leaderboardEntry.setInvalidRunReason(
                                (leaderboardEntry.getInvalidRunReason() == null) ?
                                        InvalidRunReasons.BETTER_ENTRY_WITH_SAME_NAME.toString() :
                                        leaderboardEntry.getInvalidRunReason() + "," + InvalidRunReasons.BETTER_ENTRY_WITH_SAME_NAME
                        );
                    }
                }
                Double customTopSpeed = customTopSpeeds.get(track.getGuid());
                boolean isInvalidSpeed = (customTopSpeed != null && leaderboardEntry.getTopSpeed() > customTopSpeed) ||
                        (customTopSpeed == null && leaderboardEntry.getTopSpeed() > 104);
                if (isInvalidSpeed) {
                    LOG.info("Player " + playerForEntry.getPlayerName() + " has impossible top speed " + leaderboardEntry.getTopSpeed() + ", DRL BUG!");
                    leaderboardEntry.setIsInvalidRun(true);
                    leaderboardEntry.setInvalidRunReason(
                            (leaderboardEntry.getInvalidRunReason() == null) ?
                                    InvalidRunReasons.IMPOSSIBLE_TOP_SPEED.toString() :
                                    leaderboardEntry.getInvalidRunReason() + "," + InvalidRunReasons.IMPOSSIBLE_TOP_SPEED
                    );
                }
                if (leaderboardEntry.getReplayUrl() == null) {
                    LOG.info("Player " + playerForEntry.getPlayerName() + " has no replay url, DRL BUG (Or intended)!");
                    leaderboardEntry.setIsInvalidRun(true);
                    leaderboardEntry.setInvalidRunReason(
                            (leaderboardEntry.getInvalidRunReason() == null) ?
                                    InvalidRunReasons.NO_REPLAY.toString() :
                                    leaderboardEntry.getInvalidRunReason() + "," + InvalidRunReasons.NO_REPLAY
                    );
                }
                if (doubleAccountMatchingMap.containsKey(playerForEntry.getPlayerId())) {
                    List<String> doubleAccs = doubleAccountMatchingMap.get(playerForEntry.getPlayerId());
                    for (String doubleAcc : doubleAccs) {
                        if (alreadyFoundPlayerIds.containsKey(doubleAcc)) {
                            LeaderboardEntry alreadyExistingEntry = alreadyFoundPlayerIds.get(doubleAcc);
                            if (Boolean.FALSE.equals(alreadyExistingEntry.getIsInvalidRun())) {
                                LOG.info("Player " + playerForEntry.getPlayerId() + " is a double account of " + doubleAcc + " and " +
                                        "already exists in this leaderboard");
                                leaderboardEntry.setIsInvalidRun(true);
                                leaderboardEntry.setInvalidRunReason(
                                        (leaderboardEntry.getInvalidRunReason() == null) ?
                                                InvalidRunReasons.BETTER_ENTRY_WITH_KNOWN_DOUBLE_ACCOUNT.toString() :
                                                leaderboardEntry.getInvalidRunReason() + "," + InvalidRunReasons.BETTER_ENTRY_WITH_KNOWN_DOUBLE_ACCOUNT
                                );
                                break;
                            }
                        }
                    }
                }

                // Manual invalidation
                InvalidRunReasons invalidRunReasons = manualInvalidRuns.get(leaderboardEntry.getDrlId());
                if (invalidRunReasons != null) {
                    LOG.info("Player " + playerForEntry.getPlayerName() + " is manually invalidated");
                    leaderboardEntry.setIsInvalidRun(true);
                    leaderboardEntry.setInvalidRunReason(
                            (leaderboardEntry.getInvalidRunReason() == null) ?
                                    invalidRunReasons.toString() :
                                    leaderboardEntry.getInvalidRunReason() + "," + invalidRunReasons
                    );
                }

                if (track.getDrlTrackId() != null && !track.getDrlTrackId().equals(drlLeaderboardEntry.get("track"))) {
                    LOG.info("Player " + playerForEntry.getPlayerName() + " has a replay that doesn't match the track, DRL BUG (Or " +
                            "intended)!");
                    leaderboardEntry.setIsInvalidRun(true);
                    leaderboardEntry.setInvalidRunReason(
                            (leaderboardEntry.getInvalidRunReason() == null) ?
                                    InvalidRunReasons.WRONG_REPLAY.toString() :
                                    leaderboardEntry.getInvalidRunReason() + "," + InvalidRunReasons.WRONG_REPLAY
                    );
                }

                if (track.getDrlTrackId() != null && !track.getDrlTrackId().equals(drlLeaderboardEntry.get("track"))) {
                    LOG.info("Player " + playerForEntry.getPlayerName() + " has a replay that doesn't match the track, DRL BUG (Or " +
                            "intended)!");
                    leaderboardEntry.setIsInvalidRun(true);
                    leaderboardEntry.setInvalidRunReason(
                            (leaderboardEntry.getInvalidRunReason() == null) ?
                                    InvalidRunReasons.WRONG_REPLAY.toString() :
                                    leaderboardEntry.getInvalidRunReason() + "," + InvalidRunReasons.WRONG_REPLAY
                    );
                }

                if (leaderboardEntry.getScore() != null &&
                        (leaderboardEntry.getScore() < 14400L
                                || (leaderboardEntry.getScore() < 24000L
                                    && !"STRAIGHT LINE".equals(track.getName())
                                    && !"MT-513".equals(track.getGuid()) // DRL - Sandbox
                                    && !"MGP UTT 5: NAUTILUS".equals(track.getName())
                                    && !"MGP 2018 IO ROOKIE".equals(track.getName())))) {
                    LOG.info("Player " + playerForEntry.getPlayerName() + " has a replay that is too short");
                    leaderboardEntry.setIsInvalidRun(true);
                    leaderboardEntry.setInvalidRunReason(
                            (leaderboardEntry.getInvalidRunReason() == null) ?
                                    InvalidRunReasons.WRONG_REPLAY.toString() :
                                    leaderboardEntry.getInvalidRunReason() + "," + InvalidRunReasons.WRONG_REPLAY
                    );
                }

                leaderboardEntry.setPoints(PointsCalculation.calculatePointsByPositionV3(leaderboardPosition));

                // DRL bug, that later adds a replay to the existing entry
                if (existingEntry != null
                    && leaderboardEntry.getDrlId().equals(existingEntry.getDrlId())
                    && Boolean.TRUE.equals(existingEntry.getIsInvalidRun()))
                {
                    leaderboardEntry.setPreviousPosition(existingEntry.getPreviousPosition());
                    leaderboardEntry.setPreviousScore(existingEntry.getPreviousScore());
                } else if (existingEntry != null && !LeaderboardEntry.equalsForUpdate(existingEntry, leaderboardEntry)){
                    leaderboardEntry.setPreviousPosition(existingEntry.getPosition());
                    leaderboardEntry.setPreviousScore(existingEntry.getScore());
                }
                leaderboardEntry.setPosition(leaderboardPosition);

                if (Boolean.FALSE.equals(leaderboardEntry.getIsInvalidRun()) && leaderboardEntry.getPosition().equals(1L)) {
                    leaderScore = leaderboardEntry.getScore();
                }

                if (leaderboardProcessor != null) {
                    leaderboardProcessor.process(
                            isNewTrack,
                            drlLeaderboardEntry,
                            existingEntry,
                            leaderboardEntry,
                            currentLeaderboardEntries,
                            leaderScore
                    );
                }

                LOG.trace(leaderboardEntry.toString());
                if (LeaderboardEntry.equalsForUpdate(existingEntry, leaderboardEntry)) {
                    leaderboardProcessorResult.getUnchangedLeaderboardEntries().add(leaderboardEntry);
                } else {
                    leaderboardProcessorResult.getNewOrUpdatedLeaderboardEntries().add(leaderboardEntry);
                }
                if (playerForEntry.getId() == null) {
                    playerForEntry.setCreatedAt(leaderboardEntry.getUpdatedAt());
                    playerForEntry.setUpdatedAt(leaderboardEntry.getUpdatedAt());
                    leaderboardProcessorResult.getNewPlayers().add(playerForEntry);
                } else if (playerForEntry.getUpdatedAt().isBefore(leaderboardEntry.getUpdatedAt())
                        && !Player.equalsForUpdate(playerForEntry, existingPlayer)
                ) {
                    playerForEntry.setUpdatedAt(leaderboardEntry.getUpdatedAt());
                    leaderboardProcessorResult.getUpdatedPlayers().add(playerForEntry);
                }
                alreadyFoundPlayerIds.put(playerForEntry.getPlayerId(), leaderboardEntry);
                alreadyFoundPlayerNames.put(playerForEntry.getPlayerName(), leaderboardEntry);
                currentLeaderboardEntries.remove(playerForEntry.getPlayerId());

                if (Boolean.FALSE.equals(leaderboardEntry.getIsInvalidRun())) {
                    leaderboardPosition++;
                }

                if (leaderboardPosition > maxEntriesToProcess) {
                    break;
                }
            }

            nextPageUrl = (String) paging.get("next-page-url");
            page++;
            Thread.sleep(durationBetweenRequests.toMillis());
        } while (leaderboardPosition <= maxEntriesToProcess && nextPageUrl != null);

        leaderboardProcessorResult.setExistingLeaderboardEntriesThatWerentProcessedAgain(currentLeaderboardEntries.values());
        return leaderboardProcessorResult;
    }

    private Map<String, Player> getExistingPlayersForDrlPlayerIdsOfTrack(Map<String, LeaderboardEntry> existingLeaderboardEntriesForTrack
            , List<String> playerIdsFromApiPage) {
        final Set<String> existingPlayerIds = existingLeaderboardEntriesForTrack.keySet();
        final List<String> missingPlayerIds = playerIdsFromApiPage.stream()
                .filter(playerId -> !existingPlayerIds.contains(playerId))
                .collect(Collectors.toList());
        final Map<String, Player> missingPlayersMap = playerService.getPlayersByDrlIdAsMap(missingPlayerIds);
        final Map<String, Player> existingPlayersForDrlPlayerIdsOfTrack = existingLeaderboardEntriesForTrack.values().stream()
                .map(LeaderboardEntry::getPlayer)
                .collect(Collectors.toMap(Player::getPlayerId, Function.identity()));
        existingPlayersForDrlPlayerIdsOfTrack.putAll(missingPlayersMap);
        return existingPlayersForDrlPlayerIdsOfTrack;
    }

    public LinkedHashMap<String, Object> getMapDetails(String trackId) {
        Optional<Track> byId = tracksService.findById(Long.parseLong(trackId));
        if (byId.isEmpty()) {
            throw new RuntimeException("Could not find track with id " + trackId + ".");
        }
        String guid = byId.get().getGuid();
//        guid = "CMP-0c57443b36b17c5022130b5a"; // THE LOOP
//        guid = "CMP-1630238b227713aeed900973"; // Arround Arround
        UriComponentsBuilder mapsEndpointBuilder = UriComponentsBuilder.fromUriString(mapDetailEndpoint);
        String requestUrl = mapsEndpointBuilder.buildAndExpand(Map.of("token", token, "guid", guid)).toUriString();
        LOG.info("Requesting map details for guid " + guid + " from " + requestUrl + ".");
        ResponseEntity<Map<String, Object>> responseEntity = new RestTemplate().exchange(
                requestUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        Map<String, Object> response = responseEntity.getBody();
        if (response == null || response.get("success") == null || !(boolean) response.get("success")) {
            throw new RuntimeException("Could not get map details for guid " + guid + ".");
        }

        //noinspection unchecked
        return ((List<LinkedHashMap<String, Object>>) ((LinkedHashMap<String, Object>) response.get("data")).get("data")).getFirst();
    }
}

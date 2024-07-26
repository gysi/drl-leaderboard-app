package de.gregord.drlleaderboardbackend.services;

import de.gregord.drlleaderboardbackend.domain.InvalidRunReasons;
import de.gregord.drlleaderboardbackend.domain.PointsCalculation;
import de.gregord.drlleaderboardbackend.entities.LeaderboardEntry;
import de.gregord.drlleaderboardbackend.entities.Player;
import de.gregord.drlleaderboardbackend.entities.Track;
import de.gregord.drlleaderboardbackend.entities.TrackMinimal;
import de.gregord.drlleaderboardbackend.replay.ReplayAnalyzer;
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

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

@Service
public class DRLApiService {
    public static final Logger LOG = LoggerFactory.getLogger(DRLApiService.class);
    private final String token;
    private final String mapDetailEndpoint;
    private final String leaderboardEndpoint;
    private final PlayerService playerService;
    private final TracksService tracksService;
    private final CommunitySeasonService communitySeasonService;
    private final LeaderboardRepository leaderboardRepository;
    private final ModelMapper modelMapper;
    private final Duration durationBetweenRequests;
    private Long waitMillisToResetRequestLimit = null;
    private LocalDateTime lastApiRequest = LocalDateTime.now();
    private final ReentrantLock lockApiWait = new ReentrantLock();
    private static final Map<String, Double> customTopSpeeds = new HashMap<>();
    // <player_id, player_id>
    private static final Map<String, List<String>> doubleAccountMatchingMap = new HashMap<>();
    private static final Map<String, InvalidRunReasons> manualInvalidRuns = new HashMap<>();

    public record ProcessingLbEntry(
            Map<String, Object> drlLeaderboardEntry,
            LeaderboardEntry existingEntry,
            LeaderboardEntry newOrUpdatedLeaderboardEntry,
            Player existingPlayer,
            Player player
    ) {

        public Long getNewOrUpdatedLeaderboardEntryScorePlusPenalty() {
            if(newOrUpdatedLeaderboardEntry.getTimePenaltyTotal() == null){
                return newOrUpdatedLeaderboardEntry.getScore();
            }
            return newOrUpdatedLeaderboardEntry.getScore() + newOrUpdatedLeaderboardEntry.getTimePenaltyTotal();
        }

        public LocalDateTime getNewOrUpdatedLeaderboardEntryCreatedAt() {
            return newOrUpdatedLeaderboardEntry.getCreatedAt();
        }
    }

    static {
        customTopSpeeds.put("MT-9eb", 104.6);
        customTopSpeeds.put("CMP-7ab877a2c62446a10c9316a0", 104.2); // SHIPYARD DISASTER
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
        // BDUBARI on DRL 2022-23 USAF
        manualInvalidRuns.put("663765ce191c13001769c2b5", InvalidRunReasons.WRONG_REPLAY);
    }

    public DRLApiService(
            @Value("${app.drl-api.token}") String token,
            @Value("${app.drl-api.map-detail-endpoint}") String mapDetailEndpoint,
            @Value("${app.drl-api.leaderboard-endpoint}") String leaderboardEndpoint,
            @Value("${app.drl-api.duration-between-requests}") Duration durationBetweenRequests,
            PlayerService playerService,
            TracksService tracksService,
            CommunitySeasonService communitySeasonService,
            LeaderboardRepository leaderboardRepository,
            ModelMapper modelMapper
    ) {
        this.token = token;
        this.mapDetailEndpoint = mapDetailEndpoint;
        this.leaderboardEndpoint = leaderboardEndpoint;
        this.durationBetweenRequests = durationBetweenRequests;
        this.playerService = playerService;
        this.tracksService = tracksService;
        this.communitySeasonService = communitySeasonService;
        this.leaderboardRepository = leaderboardRepository;
        this.modelMapper = modelMapper;

    }

    public <T> ResponseEntity<T> waitForApiLimitAndExecuteRequest(Supplier<ResponseEntity<T>> requestExecutor) throws RestClientException {
        lockApiWait.lock();
        try {
            while(true) {
                LocalDateTime now = LocalDateTime.now();
                long millisBetweenRequest = durationBetweenRequests.toMillis();
                long millisPassedSinceLastRequest =
                        Duration.between(
                                lastApiRequest.atZone(ZoneId.systemDefault()).toInstant(),
                                now.atZone(ZoneId.systemDefault()).toInstant()
                        ).toMillis();
                long waitTime = 0;

                if (waitMillisToResetRequestLimit != null) {
                    waitTime = waitMillisToResetRequestLimit;
                    waitMillisToResetRequestLimit = null;
                } else if (millisPassedSinceLastRequest < millisBetweenRequest) {
                    waitTime = millisBetweenRequest - millisPassedSinceLastRequest;
                }

                if (waitTime > 0) {
                    try {
                        LOG.info("Waiting for api rate limit to reset {}ms", waitTime);
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        LOG.error("Thread was interrupted while waiting to respect API limit", e);
                    }
                }

                lastApiRequest = LocalDateTime.now();
                try {
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
            }
        } finally {
            lockApiWait.unlock();
        }
    }

    private Map<String, Object> getDRLLeaderboardEntriesByPage(Track track, int page, int pageLimit) throws Exception {
        UriComponentsBuilder trackEndpointBuilder = UriComponentsBuilder.fromUriString(leaderboardEndpoint);
        if (track.getGuid().startsWith("CMP")) {
            trackEndpointBuilder
                    .queryParam("custom-map", track.getGuid())
                    .queryParam("is-custom-map", "true");
        } else {
            trackEndpointBuilder
                    .queryParam("track", track.getGuid())
                    .queryParam("map", track.getMapId())
                    .queryParam("is-custom-map", "false");
        }

        String requestUrl = trackEndpointBuilder.buildAndExpand(Map.of("token", token, "page", page, "limit", pageLimit)).toUriString();
        LOG.info("requestUrl: {}", requestUrl);
        ResponseEntity<Map<String, Object>> exchange =
                waitForApiLimitAndExecuteRequest(() -> (new RestTemplate()).exchange(requestUrl, HttpMethod.GET, null,
                        new ParameterizedTypeReference<>() {
                        }));
//        String content_length = Optional.ofNullable(exchange.getHeaders().get("Content-Length")).map(List::getFirst).orElse("0");
//        leaderboardProcessorResult.setResponseContentLength(Long.parseLong(content_length.trim()));
        Map<String, Object> response = exchange.getBody();

        if (response == null || response.get("success") == null || !(boolean) response.get("success")) {
            LOG.error("Error while fetching leaderboard for track " + track.getName() + " with map id " + track.getMapId() + " and " +
                    "track id " + track.getDrlTrackId() + " on page " + page);
            throw new Exception();
        }
        @SuppressWarnings("unchecked") Map<String, Object> data = (Map<String, Object>) response.get("data");

        //noinspection unchecked
        if (((List<Map<String, Object>>) data.get("leaderboard")).isEmpty()) {
            LOG.warn("No leaderboard entries found for track " + track.getName() + " with map id " + track.getMapId() + " and track " +
                    "id " + track.getDrlTrackId() + " on page " + page);
        }

        return data;
    }

    private List<Map<String, Object>> getDRLLeaderboardEntries(Track track, int pageLimit, int maxEntriesToGetFromTheApi) throws Exception {
        int page = 1;
        String nextPageUrl;
        long count = 0;
        List<Map<String, Object>> accumulatedDrlLeaderboardEntries = new ArrayList<>();
        do {
            Map<String, Object> data = getDRLLeaderboardEntriesByPage(track, page, pageLimit);
            @SuppressWarnings("unchecked") Map<String, Object> paging = (Map<String, Object>) data.get("pagging");
            @SuppressWarnings("unchecked") List<Map<String, Object>> drlLeaderboardEntries = (List<Map<String, Object>>) data.get("leaderboard");
            accumulatedDrlLeaderboardEntries.addAll(drlLeaderboardEntries);
            nextPageUrl = (String) paging.get("next-page-url");
            page++;
            count = count + drlLeaderboardEntries.size();
            Thread.sleep(durationBetweenRequests.toMillis());
        } while (count <= maxEntriesToGetFromTheApi && nextPageUrl != null);
        return accumulatedDrlLeaderboardEntries;
    }

    private List<ProcessingLbEntry> createProcessingLbEntries(
            Map<String, LeaderboardEntry> currentLeaderboardEntries,
            List<Map<String, Object>> drlLeaderboardEntries
    ){
        final List<String> playerIdsFromApiPage = drlLeaderboardEntries.stream().map(entry -> (String) entry.get("player-id")).toList();
        final Map<String, Player> existingPlayersForDrlPlayerIdsOfTrack =
                getExistingPlayersForDrlPlayerIdsOfTrack(currentLeaderboardEntries, playerIdsFromApiPage);

        return drlLeaderboardEntries.stream().map(drlLeaderboardEntry -> {
            Optional<LeaderboardEntry> existingEntryOpt =
                    Optional.ofNullable(currentLeaderboardEntries.get((String) drlLeaderboardEntry.get("player-id")));
            LeaderboardEntry leaderboardEntry = existingEntryOpt.orElseGet(LeaderboardEntry::new);
            Player playerForEntry;
            LeaderboardEntry existingEntry = null;
            Player existingPlayer = null;
            if (existingEntryOpt.isPresent()) {
                existingPlayer = leaderboardEntry.getPlayer();
                playerForEntry = Player.simpleCopy(leaderboardEntry.getPlayer());
                existingEntry = LeaderboardEntry.simpleCopy(existingEntryOpt.get());
            } else {
                existingPlayer = existingPlayersForDrlPlayerIdsOfTrack.get((String) drlLeaderboardEntry.get("player-id"));
                playerForEntry = Optional.ofNullable(existingPlayer).map(Player::simpleCopy).orElseGet(Player::new);
                leaderboardEntry.setPlayer(playerForEntry);
            }
            return new ProcessingLbEntry(drlLeaderboardEntry, existingEntry, leaderboardEntry, existingPlayer, playerForEntry);
        }).toList();
    }

    private void mapJsonValuesToLeaderboardEntries(Track track, List<ProcessingLbEntry> processingLbEntries) {
        for (ProcessingLbEntry processingLbEntry : processingLbEntries) {
            processingLbEntry.player.setPlayerId((String) processingLbEntry.drlLeaderboardEntry.get("player-id"));
            processingLbEntry.player.setPlayerName((String) processingLbEntry.drlLeaderboardEntry.get("username"));
            processingLbEntry.player.setFlagUrl((String) processingLbEntry.drlLeaderboardEntry.get("flag-url"));
            processingLbEntry.player.setProfilePlatform((String) processingLbEntry.drlLeaderboardEntry.get("profile-platform"));
            processingLbEntry.player.setProfilePlatformId((String) processingLbEntry.drlLeaderboardEntry.get("profile-platform-id"));
            processingLbEntry.player.setProfileThumb((String) processingLbEntry.drlLeaderboardEntry.get("profile-thumb"));
            processingLbEntry.newOrUpdatedLeaderboardEntry.setDrlId((String) processingLbEntry.drlLeaderboardEntry.get("id"));
            processingLbEntry.newOrUpdatedLeaderboardEntry.setTrack(modelMapper.map(track, TrackMinimal.class));
            processingLbEntry.newOrUpdatedLeaderboardEntry.setPlayerIdDrl((String) processingLbEntry.drlLeaderboardEntry.get("player-id"));
            processingLbEntry.newOrUpdatedLeaderboardEntry.setCrashCount((Integer) processingLbEntry.drlLeaderboardEntry.get("crash-count"));
            processingLbEntry.newOrUpdatedLeaderboardEntry.setScore(Long.valueOf((Integer) processingLbEntry.drlLeaderboardEntry.get("score")));
            processingLbEntry.newOrUpdatedLeaderboardEntry.setDroneName((String) processingLbEntry.drlLeaderboardEntry.get("drone-name"));
            processingLbEntry.newOrUpdatedLeaderboardEntry.setTopSpeed(((Number) processingLbEntry.drlLeaderboardEntry.get("top-speed")).doubleValue());
            processingLbEntry.newOrUpdatedLeaderboardEntry.setReplayUrl((String) processingLbEntry.drlLeaderboardEntry.get("replay-url"));
            processingLbEntry.newOrUpdatedLeaderboardEntry.setCreatedAt(LocalDateTime.from(ZonedDateTime.parse((String) processingLbEntry.drlLeaderboardEntry.get("created-at"))));
            processingLbEntry.newOrUpdatedLeaderboardEntry.setUpdatedAt(LocalDateTime.from(ZonedDateTime.parse((String) processingLbEntry.drlLeaderboardEntry.get("updated-at"))));
            processingLbEntry.newOrUpdatedLeaderboardEntry.setInvalidRunReason(null);
            processingLbEntry.newOrUpdatedLeaderboardEntry.setIsInvalidRun(false);
        }
    }

    private void analyzeReplays(Track track, List<ProcessingLbEntry> processingLbEntries){
        processingLbEntries.stream()
                .limit(25)
                .filter(entry -> entry.newOrUpdatedLeaderboardEntry.getReplayUrl() != null)
                .filter(entry -> {
                    // relpay was already analyzed
                    return entry.existingEntry == null
                            || (entry.existingEntry.getReplayUrl() != null
                            && !entry.existingEntry.getReplayUrl().equals(entry.newOrUpdatedLeaderboardEntry.getReplayUrl())
                    )
                            || !entry.newOrUpdatedLeaderboardEntry.getIsReplayAnalyzed();
                })
                .forEach(entry -> {
                    List<LeaderboardEntry.Penalty> penaltiesFromReplay = null;
                    try {
                        penaltiesFromReplay = ReplayAnalyzer.getPenaltiesFromReplay(entry.newOrUpdatedLeaderboardEntry.getReplayUrl());
                    } catch (IOException e) {
                        LOG.error("IO Exception while trying to analyze the replay file", e);
                        return;
                    } catch (DataFormatException e) {
                        LOG.error("DataFormatException while trying to analyze the replay file", e);
                        return;
                    } catch (ReplayAnalyzer.EmptyReplayException e) {
                        entry.newOrUpdatedLeaderboardEntry.setIsInvalidRun(true);
                        entry.newOrUpdatedLeaderboardEntry.setInvalidRunReason(
                                (entry.newOrUpdatedLeaderboardEntry.getInvalidRunReason() == null) ?
                                        InvalidRunReasons.EMPTY_REPLAY.toString() :
                                        entry.newOrUpdatedLeaderboardEntry.getInvalidRunReason() + "," + InvalidRunReasons.EMPTY_REPLAY
                        );
                        entry.newOrUpdatedLeaderboardEntry.setIsReplayAnalyzed(true);
                        return;
                    } catch (Exception e) {
                        LOG.error("Exception while analysing a replay, track: {} replay: {} player: {}", track.getName(), entry.newOrUpdatedLeaderboardEntry.getReplayUrl(), entry.player.getPlayerName(), e);
                        return;
                    }
                    entry.newOrUpdatedLeaderboardEntry.setIsReplayAnalyzed(true);
                    entry.newOrUpdatedLeaderboardEntry.setPenalties(penaltiesFromReplay);
                    Integer totalTimePenalty = penaltiesFromReplay.stream()
                            .map(LeaderboardEntry.Penalty::getTimePenalty)
                            .reduce(0, Integer::sum);
                    entry.newOrUpdatedLeaderboardEntry.setTimePenaltyTotal(totalTimePenalty);
        });
    }

    private List<ProcessingLbEntry> setOrRemoveInvalidRuns(Track track, List<ProcessingLbEntry> processingLbEntries) {
        Map<String, LeaderboardEntry> alreadyFoundPlayerNames = new HashMap<>();
        Map<String, LeaderboardEntry> alreadyFoundPlayerIds = new HashMap<>();
        Double customTopSpeed = customTopSpeeds.get(track.getGuid());
        Set<ProcessingLbEntry> markedForRemoval = new HashSet<>();

        for (ProcessingLbEntry entry : processingLbEntries) {
            if (alreadyFoundPlayerIds.containsKey(entry.player.getPlayerId())) {
                LOG.warn("Player " + entry.player.getPlayerId() + " already exists in this leaderboard, DRL BUG!");
                markedForRemoval.add(entry);
            }

            if (entry.newOrUpdatedLeaderboardEntry.getReplayUrl() == null) {
                LOG.info("Player " + entry.player.getPlayerName() + " has no replay url, DRL BUG (Or intended)!");
                entry.newOrUpdatedLeaderboardEntry.setIsInvalidRun(true);
                entry.newOrUpdatedLeaderboardEntry.setInvalidRunReason(
                        (entry.newOrUpdatedLeaderboardEntry.getInvalidRunReason() == null) ?
                                InvalidRunReasons.NO_REPLAY.toString() :
                                entry.newOrUpdatedLeaderboardEntry.getInvalidRunReason() + "," + InvalidRunReasons.NO_REPLAY
                );
            }

            if (alreadyFoundPlayerNames.containsKey(entry.player.getPlayerName())) {
                LeaderboardEntry alreadyExistingEntry = alreadyFoundPlayerNames.get(entry.player.getPlayerName());
                if (Boolean.FALSE.equals(alreadyExistingEntry.getIsInvalidRun())) {
                    LOG.warn("Playername " + entry.player.getPlayerName() + " (" + entry.player.getPlayerId() + ") already exists" +
                            " in this leaderboard (id: " + alreadyExistingEntry.getId() + ")");
                    entry.newOrUpdatedLeaderboardEntry.setIsInvalidRun(true);
                    entry.newOrUpdatedLeaderboardEntry.setInvalidRunReason(
                            (entry.newOrUpdatedLeaderboardEntry.getInvalidRunReason() == null) ?
                                    InvalidRunReasons.BETTER_ENTRY_WITH_SAME_NAME.toString() :
                                    entry.newOrUpdatedLeaderboardEntry.getInvalidRunReason() + "," + InvalidRunReasons.BETTER_ENTRY_WITH_SAME_NAME
                    );
                }
            }

            boolean isInvalidSpeed = (customTopSpeed != null && entry.newOrUpdatedLeaderboardEntry.getTopSpeed() > customTopSpeed) ||
                    (customTopSpeed == null && entry.newOrUpdatedLeaderboardEntry.getTopSpeed() > 104.15);
            if (isInvalidSpeed) {
                LOG.info("Player " + entry.player.getPlayerName() + " has impossible top speed " + entry.newOrUpdatedLeaderboardEntry.getTopSpeed() + ", DRL BUG!");
                entry.newOrUpdatedLeaderboardEntry.setIsInvalidRun(true);
                entry.newOrUpdatedLeaderboardEntry.setInvalidRunReason(
                        (entry.newOrUpdatedLeaderboardEntry.getInvalidRunReason() == null) ?
                                InvalidRunReasons.IMPOSSIBLE_TOP_SPEED.toString() :
                                entry.newOrUpdatedLeaderboardEntry.getInvalidRunReason() + "," + InvalidRunReasons.IMPOSSIBLE_TOP_SPEED
                );
            }

            if (doubleAccountMatchingMap.containsKey(entry.player.getPlayerId())) {
                List<String> doubleAccs = doubleAccountMatchingMap.get(entry.player.getPlayerId());
                for (String doubleAcc : doubleAccs) {
                    if (alreadyFoundPlayerIds.containsKey(doubleAcc)) {
                        LeaderboardEntry alreadyExistingEntry = alreadyFoundPlayerIds.get(doubleAcc);
                        if (Boolean.FALSE.equals(alreadyExistingEntry.getIsInvalidRun())) {
                            LOG.info("Player " + entry.player.getPlayerId() + " is a double account of " + doubleAcc + " and " +
                                    "already exists in this leaderboard");
                            entry.newOrUpdatedLeaderboardEntry.setIsInvalidRun(true);
                            entry.newOrUpdatedLeaderboardEntry.setInvalidRunReason(
                                    (entry.newOrUpdatedLeaderboardEntry.getInvalidRunReason() == null) ?
                                            InvalidRunReasons.BETTER_ENTRY_WITH_KNOWN_DOUBLE_ACCOUNT.toString() :
                                            entry.newOrUpdatedLeaderboardEntry.getInvalidRunReason() + "," + InvalidRunReasons.BETTER_ENTRY_WITH_KNOWN_DOUBLE_ACCOUNT
                            );
                            break;
                        }
                    }
                }
            }

            // Manual invalidation
            InvalidRunReasons invalidRunReasons = manualInvalidRuns.get(entry.newOrUpdatedLeaderboardEntry.getDrlId());
            if (invalidRunReasons != null) {
                LOG.info("Player " + entry.player.getPlayerName() + " is manually invalidated");
                entry.newOrUpdatedLeaderboardEntry.setIsInvalidRun(true);
                entry.newOrUpdatedLeaderboardEntry.setInvalidRunReason(
                        (entry.newOrUpdatedLeaderboardEntry.getInvalidRunReason() == null) ?
                                invalidRunReasons.toString() :
                                entry.newOrUpdatedLeaderboardEntry.getInvalidRunReason() + "," + invalidRunReasons
                );
            }

            if (track.getDrlTrackId() != null && !track.getDrlTrackId().equals(entry.drlLeaderboardEntry.get("track"))) {
                LOG.info("Player " + entry.player.getPlayerName() + " has a replay that doesn't match the track, DRL BUG (Or " +
                        "intended)!");
                entry.newOrUpdatedLeaderboardEntry.setIsInvalidRun(true);
                entry.newOrUpdatedLeaderboardEntry.setInvalidRunReason(
                        (entry.newOrUpdatedLeaderboardEntry.getInvalidRunReason() == null) ?
                                InvalidRunReasons.WRONG_REPLAY.toString() :
                                entry.newOrUpdatedLeaderboardEntry.getInvalidRunReason() + "," + InvalidRunReasons.WRONG_REPLAY
                );
            }

            if (track.getDrlTrackId() != null && !track.getDrlTrackId().equals(entry.drlLeaderboardEntry.get("track"))) {
                LOG.info("Player " + entry.player.getPlayerName() + " has a replay that doesn't match the track, DRL BUG (Or " +
                        "intended)!");
                entry.newOrUpdatedLeaderboardEntry.setIsInvalidRun(true);
                entry.newOrUpdatedLeaderboardEntry.setInvalidRunReason(
                        (entry.newOrUpdatedLeaderboardEntry.getInvalidRunReason() == null) ?
                                InvalidRunReasons.WRONG_REPLAY.toString() :
                                entry.newOrUpdatedLeaderboardEntry.getInvalidRunReason() + "," + InvalidRunReasons.WRONG_REPLAY
                );
            }

            if (entry.newOrUpdatedLeaderboardEntry.getScore() != null &&
                    (entry.newOrUpdatedLeaderboardEntry.getScore() < 14400L
                            || (entry.newOrUpdatedLeaderboardEntry.getScore() < 24000L
                            && !"STRAIGHT LINE".equals(track.getName())
                            && !"MT-513".equals(track.getGuid()) // DRL - Sandbox
                            && !"MGP UTT 5: NAUTILUS".equals(track.getName())
                            && !"MGP 2018 IO ROOKIE".equals(track.getName())))) {
                LOG.info("Player " + entry.player.getPlayerName() + " has a replay that is too short");
                entry.newOrUpdatedLeaderboardEntry.setIsInvalidRun(true);
                entry.newOrUpdatedLeaderboardEntry.setInvalidRunReason(
                        (entry.newOrUpdatedLeaderboardEntry.getInvalidRunReason() == null) ?
                                InvalidRunReasons.WRONG_REPLAY.toString() :
                                entry.newOrUpdatedLeaderboardEntry.getInvalidRunReason() + "," + InvalidRunReasons.WRONG_REPLAY
                );
            }

            alreadyFoundPlayerNames.put(entry.player.getPlayerName(), entry.newOrUpdatedLeaderboardEntry);
            alreadyFoundPlayerIds.put(entry.player.getPlayerId(), entry.newOrUpdatedLeaderboardEntry);
        }

        return processingLbEntries.stream().filter(entry -> !markedForRemoval.contains(entry)).toList();
    }

    public LeaderboardProcessorResult getAndProcessLeaderboardEntries(
            Track track,
            int pageLimit,
            int maxEntriesToGetFromAPI,
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



        List<Map<String, Object>> drlLeaderboardEntries = getDRLLeaderboardEntries(track, pageLimit, maxEntriesToGetFromAPI);

        // Create ProcessingLbEntries
        List<ProcessingLbEntry> processingLbEntries = createProcessingLbEntries(currentLeaderboardEntries, drlLeaderboardEntries);
        mapJsonValuesToLeaderboardEntries(track, processingLbEntries);

        processingLbEntries = setOrRemoveInvalidRuns(track, processingLbEntries);

        if(communitySeasonService.isSeasonTrack(track)) {
            analyzeReplays(track, processingLbEntries);
            // Sort by new score, now that penalties were added
            processingLbEntries = processingLbEntries.stream()
                    .sorted(Comparator.comparing(ProcessingLbEntry::getNewOrUpdatedLeaderboardEntryScorePlusPenalty)
                            .thenComparing(ProcessingLbEntry::getNewOrUpdatedLeaderboardEntryCreatedAt))
                    .toList();
        }
        // For faster debugging purposes
//        } else {
//            return leaderboardProcessorResult;
//        }

        long leaderboardPosition = 1;
        Long leaderScore = null;
        for (ProcessingLbEntry entry : processingLbEntries) {
            entry.newOrUpdatedLeaderboardEntry.setPoints(PointsCalculation.calculatePointsByPositionV3(leaderboardPosition));

            // DRL bug, that later adds a replay to the existing entry
            if (entry.existingEntry != null
                &&  entry.newOrUpdatedLeaderboardEntry.getDrlId().equals(entry.existingEntry.getDrlId())
                && Boolean.TRUE.equals(entry.existingEntry.getIsInvalidRun()))
            {
                entry.newOrUpdatedLeaderboardEntry.setPreviousPosition(entry.existingEntry.getPreviousPosition());
                entry.newOrUpdatedLeaderboardEntry.setPreviousScore(entry.existingEntry.getPreviousScore());
            } else if (entry.existingEntry != null && !LeaderboardEntry.equalsForUpdate(entry.existingEntry,  entry.newOrUpdatedLeaderboardEntry)){
                entry.newOrUpdatedLeaderboardEntry.setPreviousPosition(entry.existingEntry.getPosition());
                entry.newOrUpdatedLeaderboardEntry.setPreviousScore(entry.existingEntry.getScore());
            }
            entry.newOrUpdatedLeaderboardEntry.setPosition(leaderboardPosition);

            if (Boolean.FALSE.equals( entry.newOrUpdatedLeaderboardEntry.getIsInvalidRun()) &&  entry.newOrUpdatedLeaderboardEntry.getPosition().equals(1L)) {
                leaderScore =  entry.newOrUpdatedLeaderboardEntry.getScore();
            }

            if (leaderboardProcessor != null) {
                leaderboardProcessor.process(
                        isNewTrack,
                        entry.drlLeaderboardEntry,
                        entry.existingEntry,
                        entry.newOrUpdatedLeaderboardEntry,
                        leaderScore
                );
            }

            LOG.trace(entry.newOrUpdatedLeaderboardEntry.toString());
            if (LeaderboardEntry.equalsForUpdate(entry.existingEntry, entry.newOrUpdatedLeaderboardEntry)) {
                leaderboardProcessorResult.getUnchangedLeaderboardEntries().add(entry.newOrUpdatedLeaderboardEntry);
            } else {
                leaderboardProcessorResult.getNewOrUpdatedLeaderboardEntries().add(entry.newOrUpdatedLeaderboardEntry);
            }
            if (entry.player.getId() == null) {
                entry.player.setCreatedAt(entry.newOrUpdatedLeaderboardEntry.getUpdatedAt());
                entry.player.setUpdatedAt(entry.newOrUpdatedLeaderboardEntry.getUpdatedAt());
                leaderboardProcessorResult.getNewPlayers().add(entry.player);
            } else if (entry.player.getUpdatedAt().isBefore(entry.newOrUpdatedLeaderboardEntry.getUpdatedAt())
                    && !Player.equalsForUpdate(entry.player, entry.existingPlayer)
            ) {
                entry.player.setUpdatedAt(entry.newOrUpdatedLeaderboardEntry.getUpdatedAt());
                leaderboardProcessorResult.getUpdatedPlayers().add(entry.player);
            }
            currentLeaderboardEntries.remove(entry.player.getPlayerId());

            if (Boolean.FALSE.equals(entry.newOrUpdatedLeaderboardEntry.getIsInvalidRun())) {
                leaderboardPosition++;
            }

            if (leaderboardPosition > maxEntriesToProcess) {
                break;
            }
        }

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

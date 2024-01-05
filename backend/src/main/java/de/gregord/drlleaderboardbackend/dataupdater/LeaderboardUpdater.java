package de.gregord.drlleaderboardbackend.dataupdater;

import de.gregord.drlleaderboardbackend.domain.InvalidRunReasons;
import de.gregord.drlleaderboardbackend.domain.PlayerImprovement;
import de.gregord.drlleaderboardbackend.domain.PointsCalculation;
import de.gregord.drlleaderboardbackend.entities.LeaderboardEntry;
import de.gregord.drlleaderboardbackend.entities.Track;
import de.gregord.drlleaderboardbackend.entities.TrackMinimal;
import de.gregord.drlleaderboardbackend.repositories.LeaderboardRepository;
import de.gregord.drlleaderboardbackend.repositories.TracksRepository;
import de.gregord.drlleaderboardbackend.services.discord.DiscordMessageService;
import de.gregord.drlleaderboardbackend.services.LeaderboardService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class LeaderboardUpdater {
    public static Logger LOG = LoggerFactory.getLogger(LeaderboardUpdater.class);

    // <player_id, player_id>
    private static final Map<String, String> doubleAccountMatchingMap = new HashMap<>();
    private static final Map<String, Double> customTopSpeeds = new HashMap<>();
    private static final Map<String, InvalidRunReasons> manualInvalidRuns = new HashMap<>();

    static {
        // pawelos -> p
        doubleAccountMatchingMap.put("6376865daa4f489bfc97a2a6", "5a47746373f050000fb2e47d");
        doubleAccountMatchingMap.put("5a47746373f050000fb2e47d", "6376865daa4f489bfc97a2a6");
        // srgmajordangle
        doubleAccountMatchingMap.put("609231f81903f7802cf35459", "622d49e1a542747a374699e3");
        doubleAccountMatchingMap.put("622d49e1a542747a374699e3", "609231f81903f7802cf35459");
        // syl
        doubleAccountMatchingMap.put("628e51b31a9d7664deaa3cf3", "5c78aebaba3bdc580efdcaa0");
        doubleAccountMatchingMap.put("5c78aebaba3bdc580efdcaa0", "628e51b31a9d7664deaa3cf3");
        // cakefpv
        doubleAccountMatchingMap.put("5d5d99a7b6b937c08cbb5443", "6347b22828c01d57b57a62f1");
        doubleAccountMatchingMap.put("6347b22828c01d57b57a62f1", "5d5d99a7b6b937c08cbb5443");
        // mckfpv
        doubleAccountMatchingMap.put("5e3969116ef085dae1fd1247", "6387411d6bb427f37cc73253");
        doubleAccountMatchingMap.put("6387411d6bb427f37cc73253", "5e3969116ef085dae1fd1247");
        // halowalker
        doubleAccountMatchingMap.put("5d9c2a106ef085dae105f1e2", "6373e60eaa4f489bfc306d3d");
        doubleAccountMatchingMap.put("6373e60eaa4f489bfc306d3d", "5d9c2a106ef085dae105f1e2");
        // killian
        doubleAccountMatchingMap.put("637d4834b060342b17397788", "5988cd58d229c2460761a234");
        doubleAccountMatchingMap.put("5988cd58d229c2460761a234", "637d4834b060342b17397788");
        // alexfpv
        doubleAccountMatchingMap.put("5a0f173ecd270d000fb02b5d", "6375d300aa4f489bfcf8f9db");
        doubleAccountMatchingMap.put("6375d300aa4f489bfcf8f9db", "5a0f173ecd270d000fb02b5d");
        // animositee
        doubleAccountMatchingMap.put("638d60f86bb427f37c0c212b", "59c3b379d229c2460761c484");
        doubleAccountMatchingMap.put("59c3b379d229c2460761c484", "638d60f86bb427f37c0c212b");
        // im back :)
        doubleAccountMatchingMap.put("5a0cecc2bad69d0014b7a8fe", "634462ec28c01d57b50bee92");
        doubleAccountMatchingMap.put("634462ec28c01d57b50bee92", "5a0cecc2bad69d0014b7a8fe");

        customTopSpeeds.put("MT-9eb", 104.6);
        customTopSpeeds.put("CMP-7ab877a2c62446a10c9316a0", 104.2);
        customTopSpeeds.put("MT-fb1", 102.0); // Straight line

        // cloneno1 Moonlight #1
        manualInvalidRuns.put("62a2459bd5155d003a982cd9", InvalidRunReasons.NO_REPLAY);
        // emzies2010 Straight Line #1
        manualInvalidRuns.put("649dee068116aa000a676b8f", InvalidRunReasons.WRONG_DRONE);
        // hsufoundation5 Straight Line #2
        manualInvalidRuns.put("64af0b982e81e5002839799a", InvalidRunReasons.WRONG_DRONE);
        // filthy radish Straight Line #1
        manualInvalidRuns.put("655c78c6f5f438000a237dae", InvalidRunReasons.WRONG_DRONE);
    }

    private static Long totalContentLength = 0L;
    private static Long totalRequestCount = 0L;

    private final String token;
    private final String leaderboardEndpoint;
    private final Duration durationBetweenRequests;
    private final LeaderboardRepository leaderboardRepository;
    private final TracksRepository tracksRepository;
    private final LeaderboardService leaderboardService;
    private final ModelMapper modelMapper;
    private final CacheManager cacheManager;
    private final DiscordMessageService discordService;

    public LeaderboardUpdater(
            @Value("${app.drl-api.token}") String token,
            @Value("${app.drl-api.leaderboard-endpoint}") String leaderboardEndpoint,
            @Value("${app.drl-api.duration-between-requests}") Duration durationBetweenRequests,
            LeaderboardRepository leaderboardRepository,
            TracksRepository tracksRepository,
            LeaderboardService leaderboardService,
            ModelMapper modelMapper,
            CacheManager cacheManager,
            DiscordMessageService discordService
    ) {
        this.token = token;
        this.leaderboardEndpoint = leaderboardEndpoint;
        this.durationBetweenRequests = durationBetweenRequests;
        this.leaderboardRepository = leaderboardRepository;
        this.tracksRepository = tracksRepository;
        this.leaderboardService = leaderboardService;
        this.modelMapper = modelMapper;
        this.cacheManager = cacheManager;
        this.discordService = discordService;
    }

    @Caching(evict = {
            @CacheEvict(value = "leaderboardbyplayername", allEntries = true),
            @CacheEvict(value = "overallranking", allEntries = true),
            @CacheEvict(value = "latestLeaderboardActivity", allEntries = true),
            @CacheEvict(value = "latestLeaderboardActivityTop10", allEntries = true),
            @CacheEvict(value = "mostPbsLast7Days", allEntries = true),
            @CacheEvict(value = "mostPbsLastMonth", allEntries = true),
            @CacheEvict(value = "mostEntriesByTrackLast14Days", allEntries = true),
            @CacheEvict(value = "mostEntriesByTrackLastMonth", allEntries = true),
    })
    public void initialize() {
        long count = leaderboardRepository.count();
        if (count <= 0) {
            LOG.info("Leaderboard is empty, initializing...");
            updateLeaderboard();
        }
    }

    @Caching(evict = {
            @CacheEvict(value = "leaderboardbyplayername", allEntries = true),
            @CacheEvict(value = "overallranking", allEntries = true),
            @CacheEvict(value = "latestLeaderboardActivity", allEntries = true),
            @CacheEvict(value = "latestLeaderboardActivityTop10", allEntries = true),
            @CacheEvict(value = "mostPbsLast7Days", allEntries = true),
            @CacheEvict(value = "mostPbsLastMonth", allEntries = true),
            @CacheEvict(value = "mostEntriesByTrackLast14Days", allEntries = true),
            @CacheEvict(value = "mostEntriesByTrackLastMonth", allEntries = true),
    })
    public void updateLeaderboard() {
        totalContentLength = 0L;
        totalRequestCount = 0L;
        List<PlayerImprovement> improvements = new ArrayList<>();
        List<Track> allTracks = tracksRepository.findAll();
        for (Track track : allTracks) {
            updateLeaderboardForTrack(track, improvements);
            Optional.ofNullable(cacheManager.getCache("leaderboardbytrack"))
                    .ifPresent(cache -> cache.evictIfPresent(track.getId()));
        }
        if (!improvements.isEmpty()) {
            discordService.sendMessageToLeaderboardPostsChannels(improvements);
        }
        LOG.info("Total content length: " + totalContentLength.doubleValue() / 1024 / 1024 + " MB");
        LOG.info("Total request count: " + totalRequestCount);
    }

    public void updateLeaderboardForTrack(Track track, List<PlayerImprovement> improvements) {
        try {
            LOG.info("Updating leaderboard for track " + track.getName());
            // <PlayerId, LeaderboardDto>
            Map<String, LeaderboardEntry> currentLeaderboardEntries = new HashMap<>();
            List<LeaderboardEntry> leaderboardEntryEntriesToBeSaved = new ArrayList<>();
            leaderboardRepository.findByTrack(modelMapper.map(track, TrackMinimal.class)).forEach(leaderboard -> {
                currentLeaderboardEntries.put(leaderboard.getPlayerId(), leaderboard);
            });

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
            int maxEntries = 100;
            String nextPageUrl = null;
            long leaderboardPosition = 1;
            // <player_id, leaderboard>
            Map<String, LeaderboardEntry> alreadyFoundPlayerIds = new HashMap<>();
            // <player_name, leaderboard>
            Map<String, LeaderboardEntry> alreadyFoundPlayerNames = new HashMap<>();
            Long leaderScore = null;
            do {
                String requestUrl = mapsEndpointBuilder.buildAndExpand(Map.of("token", token, "page", page)).toUriString();
                LOG.info("requestUrl: {}", requestUrl);
                ResponseEntity<Map> exchange = (new RestTemplate()).exchange(requestUrl, HttpMethod.GET, null, Map.class);
                String content_length = exchange.getHeaders().get("Content-Length").get(0);
                totalContentLength += Long.parseLong(content_length.trim());
                totalRequestCount++;
                Map<String, Object> response = exchange.getBody();

                if (!(boolean) response.get("success")) {
                    LOG.error("Error while fetching leaderboard for track " + track.getName() + " with map id " + track.getMapId() + " and track id " + track.getDrlTrackId() + " on page " + page);
                    throw new Exception();
                }
                Map<String, Object> data = (Map<String, Object>) response.get("data");
                Map<String, Object> paging = (Map<String, Object>) data.get("pagging");
                List<Map<String, Object>> leaderboard = (List<Map<String, Object>>) data.get("leaderboard");

                if (leaderboard.size() == 0) {
                    LOG.warn("No leaderboard entries found for track " + track.getName() + " with map id " + track.getMapId() + " and track id " + track.getDrlTrackId() + " on page " + page);
                }

                // Collect by trackid and playerids as HashMap for faster lookup
                List<String> playerIdsFromApiPage = leaderboard.stream().map(entry -> (String) entry.get("player-id")).toList();
                Collection<LeaderboardEntry> byTrackIdAndPlayerIdIn = leaderboardRepository.findByTrackIdAndPlayerIdIn(track.getId(), playerIdsFromApiPage);
                Map<String, LeaderboardEntry> existingLeaderboardEntriesForTrack = byTrackIdAndPlayerIdIn.stream().collect(Collectors.toMap(LeaderboardEntry::getPlayerId, Function.identity()));

                for (Map<String, Object> drlLeaderboardEntry : leaderboard) {
                    LeaderboardEntry leaderboardEntry;
                    Optional<LeaderboardEntry> existingEntry = Optional.ofNullable(existingLeaderboardEntriesForTrack.get((String) drlLeaderboardEntry.get("player-id")));
                    Boolean isExistingEntryInvalid = existingEntry.map(LeaderboardEntry::getIsInvalidRun).orElse(null);
                    leaderboardEntry = existingEntry.orElseGet(LeaderboardEntry::new);
                    leaderboardEntry.setDrlId((String) drlLeaderboardEntry.get("id"));
                    leaderboardEntry.setTrack(modelMapper.map(track, TrackMinimal.class));
                    leaderboardEntry.setPlayerId((String) drlLeaderboardEntry.get("player-id"));
                    if (alreadyFoundPlayerIds.containsKey(leaderboardEntry.getPlayerId())) {
                        LOG.warn("Player " + leaderboardEntry.getPlayerId() + " already exists in this leaderboard, DRL BUG!");
                        continue;
                    }
                    leaderboardEntry.setPlayerName((String) drlLeaderboardEntry.get("profile-name"));
                    leaderboardEntry.setCrashCount((Integer) drlLeaderboardEntry.get("crash-count"));
                    leaderboardEntry.setScore(Long.valueOf((Integer) drlLeaderboardEntry.get("score")));
                    leaderboardEntry.setFlagUrl((String) drlLeaderboardEntry.get("flag-url"));
                    leaderboardEntry.setDroneName((String) drlLeaderboardEntry.get("drone-name"));
                    leaderboardEntry.setProfilePlatform((String) drlLeaderboardEntry.get("profile-platform"));
                    leaderboardEntry.setProfilePlatformId((String) drlLeaderboardEntry.get("profile-platform-id"));
                    leaderboardEntry.setProfileThumb((String) drlLeaderboardEntry.get("profile-thumb"));
                    leaderboardEntry.setTopSpeed((Double) drlLeaderboardEntry.get("top-speed"));
                    leaderboardEntry.setReplayUrl((String) drlLeaderboardEntry.get("replay-url"));
                    leaderboardEntry.setCreatedAt(LocalDateTime.from(ZonedDateTime.parse((String) drlLeaderboardEntry.get("created-at"))));
                    leaderboardEntry.setInvalidRunReason(null);
                    leaderboardEntry.setIsInvalidRun(false);
                    if (alreadyFoundPlayerNames.containsKey(leaderboardEntry.getPlayerName())) {
                        LeaderboardEntry alreadyExistingEntry = alreadyFoundPlayerNames.get(leaderboardEntry.getPlayerName());
                        if(!alreadyExistingEntry.getIsInvalidRun()) {
                            LOG.warn("Playername " + leaderboardEntry.getPlayerName() + " (" + leaderboardEntry.getPlayerId() + ") already exists in this leaderboard (" + alreadyExistingEntry.getPlayerId() + ")");
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
                        LOG.warn("Player " + leaderboardEntry.getPlayerName() + " has impossible top speed " + leaderboardEntry.getTopSpeed() + ", DRL BUG!");
                        leaderboardEntry.setIsInvalidRun(true);
                        leaderboardEntry.setInvalidRunReason(
                                (leaderboardEntry.getInvalidRunReason() == null) ?
                                        InvalidRunReasons.IMPOSSIBLE_TOP_SPEED.toString() :
                                        leaderboardEntry.getInvalidRunReason() + "," + InvalidRunReasons.IMPOSSIBLE_TOP_SPEED
                        );
                    }
                    if (leaderboardEntry.getReplayUrl() == null) {
                        LOG.warn("Player " + leaderboardEntry.getPlayerName() + " has no replay url, DRL BUG (Or intended)!");
                        leaderboardEntry.setIsInvalidRun(true);
                        leaderboardEntry.setInvalidRunReason(
                                (leaderboardEntry.getInvalidRunReason() == null) ?
                                        InvalidRunReasons.NO_REPLAY.toString() :
                                        leaderboardEntry.getInvalidRunReason() + "," + InvalidRunReasons.NO_REPLAY
                        );
                    }
                    if (doubleAccountMatchingMap.containsKey(leaderboardEntry.getPlayerId())) {
                        if (alreadyFoundPlayerIds.containsKey(doubleAccountMatchingMap.get(leaderboardEntry.getPlayerId()))) {
                            LeaderboardEntry alreadyExistingEntry = alreadyFoundPlayerIds.get(doubleAccountMatchingMap.get(leaderboardEntry.getPlayerId()));
                            if(!alreadyExistingEntry.getIsInvalidRun()) {
                                LOG.warn("Player " + leaderboardEntry.getPlayerId() + " is a double account of " + doubleAccountMatchingMap.get(leaderboardEntry.getPlayerId()) + " and already exists in this leaderboard");
                                leaderboardEntry.setIsInvalidRun(true);
                                leaderboardEntry.setInvalidRunReason(
                                        (leaderboardEntry.getInvalidRunReason() == null) ?
                                                InvalidRunReasons.BETTER_ENTRY_WITH_KNOWN_DOUBLE_ACCOUNT.toString() :
                                                leaderboardEntry.getInvalidRunReason() + "," + InvalidRunReasons.BETTER_ENTRY_WITH_KNOWN_DOUBLE_ACCOUNT
                                );
                            }
                        }
                    }

                    // Epic bug
                    if(leaderboardPosition == 2){
                        Optional<LeaderboardEntry> invalidEntryOpt = leaderboardEntryEntriesToBeSaved.stream()
                                .filter(lbe -> lbe.getProfilePlatform().equals("Epic"))
                                .filter(lbe -> lbe.getPosition() == 1)
                                .filter(lbe -> !lbe.getIsInvalidRun())
                                .filter(lbe -> leaderboardEntry.getScore() - lbe.getScore() >= 3000).findAny();
                        if(invalidEntryOpt.isPresent()) {
                            LeaderboardEntry invalidEntry = invalidEntryOpt.get();
                            LOG.warn("Player " + invalidEntry.getPlayerName()
                                + " has the strange epic bug (position 1 and very low time compared to position 2");
                            invalidEntry.setIsInvalidRun(true);
                            invalidEntry.setInvalidRunReason(
                                        (invalidEntry.getInvalidRunReason() == null) ?
                                                InvalidRunReasons.EPIC_BUG.toString() :
                                                invalidEntry.getInvalidRunReason() + "," + InvalidRunReasons.EPIC_BUG
                            );
                            leaderboardPosition--;
                        }
                    }

                    // Manual invalidation
                    InvalidRunReasons invalidRunReasons = manualInvalidRuns.get(leaderboardEntry.getDrlId());
                    if(invalidRunReasons != null){
                        LOG.info("Player " + leaderboardEntry.getPlayerName() + " is manually invalidated");
                        leaderboardEntry.setIsInvalidRun(true);
                        leaderboardEntry.setInvalidRunReason(
                                (leaderboardEntry.getInvalidRunReason() == null) ?
                                        invalidRunReasons.toString() :
                                        leaderboardEntry.getInvalidRunReason() + "," + invalidRunReasons
                        );
                    }

                    leaderboardEntry.setPoints(PointsCalculation.calculatePointsByPosition((double) leaderboardPosition));
                    leaderboardEntry.setPosition(leaderboardPosition);

                    if (!leaderboardEntry.getIsInvalidRun() && leaderboardEntry.getPosition().equals(1L)) {
                        leaderScore = leaderboardEntry.getScore();
                    }

                    LeaderboardEntry existingEntryInDB = currentLeaderboardEntries.get(leaderboardEntry.getPlayerId());
                    if (leaderboardPosition <= 50
                            && (existingEntryInDB == null
                                || leaderboardPosition < existingEntryInDB.getPosition()
                                /* When the DRL Api doesn't give me a replay URL I flag the run as invalid. But it happens often
                                   that the replay is there later on because the DRL System is slow to upload it and save it
                                   within the player pb. Until thats happened my site reports that PB as invalid.
                                   Now my DRL Bot goes through all new pbs that have a new position for all tracks every ~10Minutes.
                                   If there are any then it sends them to discord (But not invalid ones). It then saves the creation
                                   time from the latest PB it posted and saves it. This way I know what I already posted so that I
                                   don't post twice..
                                   Now if your run then later gets a replay attached to it, I will update your PB, but I won't post it
                                   because its creation time is before the time my bot last posted.
                                   This next condition fixes this: */
                                || (Objects.equals(isExistingEntryInvalid, Boolean.TRUE) && !leaderboardEntry.getIsInvalidRun()))
                            && !leaderboardEntry.getIsInvalidRun()) {
                        PlayerImprovement improvement = new PlayerImprovement();
                        improvement.setPlayerName(leaderboardEntry.getPlayerName());
                        improvement.setPreviousPosition(existingEntryInDB != null ? existingEntryInDB.getPosition() : null);
                        improvement.setCurrentPosition(leaderboardPosition);
                        improvement.setPreviousScore(existingEntryInDB != null ? existingEntryInDB.getScore() : null);
                        improvement.setCurrentScore(leaderboardEntry.getScore());
                        improvement.setCreatedAt(leaderboardEntry.getCreatedAt());
                        improvement.setTrack(leaderboardEntry.getTrack());
                        improvement.setProfilePicture(leaderboardEntry.getProfileThumb());
                        improvement.setLeaderScore(leaderScore);
                        if (Objects.equals(isExistingEntryInvalid, Boolean.TRUE)) {
                            improvement.setForcePost(true);
                        }
                        improvements.add(improvement);
                    }

                    if (!leaderboardEntry.getIsInvalidRun()) {
                        leaderboardPosition++;
                    }

                    LOG.trace(leaderboardEntry.toString());
                    leaderboardEntryEntriesToBeSaved.add(leaderboardEntry);
                    alreadyFoundPlayerIds.put(leaderboardEntry.getPlayerId(), leaderboardEntry);
                    alreadyFoundPlayerNames.put(leaderboardEntry.getPlayerName(), leaderboardEntry);
                    currentLeaderboardEntries.remove(leaderboardEntry.getPlayerId());
                    if(leaderboardPosition > maxEntries){
                        break;
                    }
                }

                nextPageUrl = (String) paging.get("next-page-url");
                page++;
                Thread.sleep(durationBetweenRequests.toMillis());
            } while (leaderboardPosition <= maxEntries && nextPageUrl != null);

            LOG.info("Saving leaderboard entries for track " + track.getName() + " with map id " + track.getMapId() + " and map name " + track.getMapName());
            leaderboardService.saveAndDeleteLeaderboardEntries(leaderboardEntryEntriesToBeSaved, currentLeaderboardEntries.values());
        } catch (Exception e) {
            LOG.error("Error while updating leaderboard entry", e);
            try {
                Thread.sleep(durationBetweenRequests.toMillis());
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}

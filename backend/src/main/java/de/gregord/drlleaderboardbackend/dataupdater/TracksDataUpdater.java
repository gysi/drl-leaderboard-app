package de.gregord.drlleaderboardbackend.dataupdater;

import de.gregord.drlleaderboardbackend.domain.convert.DRLTrackToTrackEntity;
import de.gregord.drlleaderboardbackend.entities.MapCategory;
import de.gregord.drlleaderboardbackend.entities.Track;
import de.gregord.drlleaderboardbackend.services.DRLApiService;
import de.gregord.drlleaderboardbackend.services.TracksService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static de.gregord.drlleaderboardbackend.config.CacheConfig.CACHE_INTERNAL_MAPID_TRACKID_MAPPING;

public abstract class TracksDataUpdater {
    protected final Logger LOG;
    private final String token;
    private final String mapsEndpoint;
    private final DRLApiService drlApiService;
    private final TracksService tracksService;
    private final CacheManager cacheManager;
    private final Set<String> mappableMapCategories;
    private final Map<String, MapCategory> mapCategoryToParentCategory;
    private final Set<Integer> mapCategoryIdsForDB;
    @Setter
    private List<String> excludedMapsByName = new ArrayList<>();
    @Setter
    private Map<String, MapCategory> mapIdToParentCategory = new HashMap<>();
    @Setter
    private List<Track> manualTracksToBeAdded = new ArrayList<>();

    public TracksDataUpdater(
            String token,
            String mapsEndpoint,
            DRLApiService drlApiService,
            TracksService tracksService,
            CacheManager cacheManager,
            Set<String> mappableMapCategories,
            Map<String, MapCategory> mapCategoryToParentCategory,
            Set<Integer> mapCategoryIdsForDB
    ) {
        this.token = token;
        this.mapsEndpoint = mapsEndpoint;
        this.drlApiService = drlApiService;
        this.tracksService = tracksService;
        this.cacheManager = cacheManager;
        this.mappableMapCategories = mappableMapCategories;
        this.mapCategoryToParentCategory = mapCategoryToParentCategory;
        this.mapCategoryIdsForDB = mapCategoryIdsForDB;
        LOG = LoggerFactory.getLogger(this.getClass());
    }

    public void initialize() {
        long count = tracksService.countByMapCategoryIdIn(mapCategoryIdsForDB);
        if (count <= 0) {
            LOG.info("No tracks found in database, initializing...");
            updateMapsData();
        }
    }

    public void updateMapsData() {
        LOG.info("Updating maps data");
        List<Track> tracksToBeSaved = new ArrayList<>();
        boolean preventDeletion = false;
        RestTemplate restTemplate = new RestTemplate();
        int pageCount = 1;
        String nextPageUrl = null;
        try {
            do {
                LOG.info("Requesting page " + pageCount);
                UriComponentsBuilder mapsEndpointBuilder = UriComponentsBuilder.fromUriString(mapsEndpoint);
                final String requestUrl = mapsEndpointBuilder.buildAndExpand(Map.of("token", token, "page", pageCount)).toUriString();
                LOG.info("requestUrl: " + requestUrl);
                try {
                    ResponseEntity<Map> exchange = drlApiService.waitForApiLimitAndExecuteRequest(() -> restTemplate.exchange(requestUrl,
                            HttpMethod.GET, null, Map.class));
                    Map<String, Object> response = exchange.getBody();

                    if ((boolean) response.get("success")) {
                        Map<String, Object> data = (Map<String, Object>) response.get("data");
                        Map<String, Object> paging = (Map<String, Object>) data.get("pagging");
                        List<Map<String, Object>> mapsData = (List<Map<String, Object>>) data.get("data");

                        List<String> guidsFromApiPage = mapsData.stream().map(track -> (String) track.get("guid")).toList();
                        Collection<Track> byGuidIn = tracksService.findByGuidIn(guidsFromApiPage);
                        Map<String, Track> existingTracksByGuid = byGuidIn.stream().collect(Collectors.toMap(Track::getGuid,
                                Function.identity()));

                        for (Map<String, Object> entry : mapsData) {
                            LOG.info("Processing Track: {}", entry.get("map-title"));
                            if (excludedMapsByName.contains(((String) entry.get("map-title")).trim().toUpperCase())) {
                                continue;
                            }
                            Optional<Track> existingTrack = Optional.ofNullable(existingTracksByGuid.get((String) entry.get("guid")));
                            if (existingTrack.isPresent()) {
                                LOG.info("Track already exists: " + existingTrack.get().getGuid());
                            } else {
                                LOG.info("New Track: " + entry.get("guid"));
                            }
                            Track track = existingTrack.orElseGet(Track::new);
                            DRLTrackToTrackEntity.setValuesToTrack(entry, track);
                            findAndSetDrlTrackIdForTrack(track);

                            MapCategory parentCategory;
                            if (mappableMapCategories.contains(track.getMapCategory())) {
                                parentCategory = mapCategoryToParentCategory.get(track.getMapCategory());
                                track.setParentCategory(parentCategory.getDescription());
                            } else if (mapIdToParentCategory.containsKey(track.getMapId())) {
                                parentCategory = mapIdToParentCategory.get(track.getMapId());
                                track.setParentCategory(parentCategory.getDescription());
                            } else {
                                LOG.error("This map category isn't registered yet, look into it. mapCategory: {} mapId: {} track: {}",
                                        track.getMapCategory(),
                                        track.getMapId(),
                                        track.getName());
                                continue;
                            }
                            track.setMapCategoryId(parentCategory.ordinal());
                            {
                                Integer mapLaps = Optional.ofNullable(track.getMapLaps()).orElse(1);
                                Double mapDistance = Optional.ofNullable(track.getMapDistance()).orElse(1000.);
                                double calculatedDistance = mapLaps * mapDistance;
                                if (calculatedDistance > 11000.) {
                                    LOG.info("Map distance is too huge, we don't want it: {} track: {}", mapDistance, track);
                                    continue;
                                }
                            }
                            tracksToBeSaved.add(track);
                        }
                        nextPageUrl = (String) paging.get("next-page-url");
                    }
                } catch (HttpServerErrorException.InternalServerError e) {
                    LOG.error("Error while requesting page " + pageCount + ": " + e.getMessage());
                    LOG.error("requestUrl: " + requestUrl);
                    preventDeletion = true;
                }
                pageCount++;
            } while (nextPageUrl != null);

            for (Track track : manualTracksToBeAdded) {
                Optional<Track> existingTrack = tracksService.findByGuid(track.getGuid());
                Track manualTrack = existingTrack.orElse(track);
                manualTrack.setDrlTrackId(track.getDrlTrackId());
                manualTrack.setGuid(track.getGuid());
                manualTrack.setName(track.getName());
                manualTrack.setMapId(track.getMapId());
                manualTrack.setMapName(DRLTrackToTrackEntity.getMapNameFromMapId(track.getMapId()));
                manualTrack.setParentCategory(mapIdToParentCategory.get(track.getMapId()).getDescription());
                manualTrack.setMapCategoryId(track.getMapCategoryId());
                tracksToBeSaved.add(manualTrack);
            }

            // Could be optimized by applying distinct only on the guid field because the API returns a specific track two times.
            // And distinct executes the equals/hashCode method and it includes the id field as well.
            // But for new tracks its null and for existing its then on both objects the id from the database
            tracksService.saveAllAndDeleteMissing(tracksToBeSaved, mapCategoryIdsForDB, preventDeletion);
        } catch (Exception e) {
            LOG.error("Error updating maps data", e);
        }
    }

    private void findAndSetDrlTrackIdForTrack(Track track) throws Exception {
        // We can only get the real drl_track_id from the leaderboard entry.
        // And it can happen that this is wrong and then we later want to flag a replay as invalid if this is the case.
        // But to make sure to set it to the right value we want to record the "track" value from the api entry
        // multiple times to make sure that there isn't a invalid replay entry within it and we save a wrong
        // drl_track_id to our track entity
        if (track.getDrlTrackId() == null) {
            {
                Map<String, String> drlTrackIdByMapId = tracksService.getDrlTrackIdByMapIdMap();
                String drlTrackId = drlTrackIdByMapId.get(track.getMapId());
                if (drlTrackId != null) {
                    track.setDrlTrackId(drlTrackId);
                    return;
                }
            }
            Map<String, Integer> trackIdOccurrences = new HashMap<>();
            drlApiService.getAndProcessLeaderboardEntries(track, 10, 5,
                    (isNewTrack,
                     drlLeaderboardEntry,
                     existingEntry,
                     newOrUpdatedLeaderboardEntry,
                     currentLeaderboardEntriesByPlayerId,
                     leaderScore) ->
                    {
                        // this is just to make sure the run is not flagged as invalid and the loop goes too much longer than
                        // maxEntriesToProcess
                        track.setDrlTrackId((String) drlLeaderboardEntry.get("track"));
                        // Record each occurrence of the track ID
                        String currentTrackId = (String) drlLeaderboardEntry.get("track");
                        trackIdOccurrences.merge(currentTrackId, 1, Integer::sum);
                    }
            );
            // After collecting all occurrences, find the most common track ID and set it to the track entity
            trackIdOccurrences.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey).ifPresent(drlTrackId -> {
                        track.setDrlTrackId(drlTrackId);
                        Map<String, String> drlTrackIdByMapIdMap = tracksService.getDrlTrackIdByMapIdMap();
                        drlTrackIdByMapIdMap.put(track.getMapId(), drlTrackId);
                        Optional.ofNullable(cacheManager.getCache(CACHE_INTERNAL_MAPID_TRACKID_MAPPING))
                                .ifPresent(cache -> cache.put(
                                        CACHE_INTERNAL_MAPID_TRACKID_MAPPING,
                                        drlTrackIdByMapIdMap)
                                );
                    });
        }
    }
}

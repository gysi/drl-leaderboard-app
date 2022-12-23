package de.gregord.drlleaderboardbackend.dataupdater;

import de.gregord.drlleaderboardbackend.entities.Track;
import de.gregord.drlleaderboardbackend.repositories.LeaderboardRepository;
import de.gregord.drlleaderboardbackend.repositories.TracksRepository;
import jakarta.transaction.Transactional;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.*;

@EnableScheduling
@Component
@Aspect
public class TracksDataUpdater {
    public static Logger LOG = LoggerFactory.getLogger(TracksDataUpdater.class);
//    public static String endpointMaps = "https://api.drlgame.com/progression/maps?token={token}";
//    public static String endpointMapDetail = "https://api.drlgame.com/maps/{guid}?token={token}";

    private static final List<String> excludedMapsByName = List.of(
            "FREESTYLE"
    );

    private static final Map<String, String> mapIdToMapName = new HashMap<>();
    private static final Map<String, String> mapIdToParentCategory = new HashMap<>();
    private static final Map<String, String> mapCategoryToParentCategory = new HashMap<>();
    private static final Set<String> mappableMapCategories = Set.of(
            "MapMultiGP",
            "MapDRLSimCup",
            "MapFeatured",
            "MapVirtualSeason"
    );

    private static final List<Track> manualTracksToBeAdded = new ArrayList<>();

    static {
        mapIdToMapName.put("MP-3fd", "HARD ROCK STADIUM");
        mapIdToMapName.put("MP-693", "CHAMPIONSHIP KINGDOM");
        mapIdToMapName.put("MP-2e1", "ALLIANZ RIVIERA");
        mapIdToMapName.put("MP-3c7", "BMW WELT");
        mapIdToMapName.put("MP-8e5", "ADVENTUREDOME");
        mapIdToMapName.put("MP-0a3", "BIOSPHERE 2");
        mapIdToMapName.put("MP-639", "SKATEPARK LA");
        mapIdToMapName.put("MP-46c", "CALIFORNIA NIGHTS");
        mapIdToMapName.put("MP-342", "2017 WORLD CHAMPIONSHIP");
        mapIdToMapName.put("MP-669", "MUNICH PLAYOFFS");
        mapIdToMapName.put("MP-0b3", "BOSTON FOUNDRY");
        mapIdToMapName.put("MP-df5", "MARDI GRAS WORLD");
        mapIdToMapName.put("MP-ed5", "DETROIT");
        mapIdToMapName.put("MP-7ed", "ATLANTA AFTERMATH");
        mapIdToMapName.put("MP-a35", "OHIO CRASHSITE");
        mapIdToMapName.put("MP-92e", "PROJECT MANHATTAN");
        mapIdToMapName.put("MP-ad9", "MIAMI LIGHTS");
        mapIdToMapName.put("MP-bf7", "L.A.POCALYPSE");

        mapIdToMapName.put("MP-2cb", "MEGA CITY");
        mapIdToMapName.put("MP-f95", "U.S. AIR FORCE BONEYARD");
        mapIdToMapName.put("MP-19c", "U.S. AIR FORCE NIGHT MODE");
        mapIdToMapName.put("MP-95a", "GATES OF NEW YORK");
        mapIdToMapName.put("MP-50c", "CAMPGROUND");
        mapIdToMapName.put("MP-615", "DRONE PARK");
        mapIdToMapName.put("MP-103", "OUT OF SERVICE");
        mapIdToMapName.put("MP-409", "THE HOUSE");
        mapIdToMapName.put("MP-23c", "DRL SANDBOX");
        mapIdToMapName.put("MP-b59", "BRIDGE");

        mapIdToMapName.put("MP-0c6", "MULTIGP");

        mapIdToParentCategory.put("MP-3fd", "DRL MAPS");
        mapIdToParentCategory.put("MP-693", "DRL MAPS");
        mapIdToParentCategory.put("MP-2e1", "DRL MAPS");
        mapIdToParentCategory.put("MP-3c7", "DRL MAPS");
        mapIdToParentCategory.put("MP-8e5", "DRL MAPS");
        mapIdToParentCategory.put("MP-0a3", "DRL MAPS");
        mapIdToParentCategory.put("MP-639", "DRL MAPS");
        mapIdToParentCategory.put("MP-46c", "DRL MAPS");
        mapIdToParentCategory.put("MP-342", "DRL MAPS");
        mapIdToParentCategory.put("MP-669", "DRL MAPS");
        mapIdToParentCategory.put("MP-0b3", "DRL MAPS");
        mapIdToParentCategory.put("MP-df5", "DRL MAPS");
        mapIdToParentCategory.put("MP-ed5", "DRL MAPS");
        mapIdToParentCategory.put("MP-7ed", "DRL MAPS");
        mapIdToParentCategory.put("MP-a35", "DRL MAPS");
        mapIdToParentCategory.put("MP-92e", "DRL MAPS");
        mapIdToParentCategory.put("MP-ad9", "DRL MAPS");
        mapIdToParentCategory.put("MP-bf7", "DRL MAPS");

        mapIdToParentCategory.put("MP-2cb", "ORIGINALS");
        mapIdToParentCategory.put("MP-f95", "ORIGINALS");
        mapIdToParentCategory.put("MP-19c", "ORIGINALS");
        mapIdToParentCategory.put("MP-95a", "ORIGINALS");
        mapIdToParentCategory.put("MP-50c", "ORIGINALS");
        mapIdToParentCategory.put("MP-615", "ORIGINALS");
        mapIdToParentCategory.put("MP-103", "ORIGINALS");
        mapIdToParentCategory.put("MP-409", "ORIGINALS");
        mapIdToParentCategory.put("MP-23c", "ORIGINALS");
        mapIdToParentCategory.put("MP-b59", "ORIGINALS");

        mapIdToParentCategory.put("MP-0c6", "MULTIGP");

        mapCategoryToParentCategory.put("MapMultiGP", "MULTIGP");
        mapCategoryToParentCategory.put("MapDRLSimCup", "DRL SIM RACING CUP");
        mapCategoryToParentCategory.put("MapFeatured", "FEATURED TRACKS");
        mapCategoryToParentCategory.put("MapVirtualSeason", "VIRTUAL SEASON");

        manualTracksToBeAdded.add(new Track("MP-ed5", "MT-418", "MT-418", "DRL 2016"));
        manualTracksToBeAdded.add(new Track("MP-a35", "MT-a72", "MT-a72", "CONFIRM NOR DENY"));
        manualTracksToBeAdded.add(new Track("MP-a35", "MT-3dd", "MT-3dd", "RESTRICTED AREA"));
        manualTracksToBeAdded.add(new Track("MP-a35", "MT-1e7", "MT-1e7", "DRL 2016"));
        manualTracksToBeAdded.add(new Track("MP-92e", "MT-2a7", "MT-2a7", "PROJECT NOOB"));
        manualTracksToBeAdded.add(new Track("MP-92e", "MT-39f", "MT-39f", "THE TRINITY TEST"));
        manualTracksToBeAdded.add(new Track("MP-92e", "MT-5db", "MT-5db", "DRL 2016"));
        manualTracksToBeAdded.add(new Track("MP-ad9", "MT-ccd", "MT-ccd", "MIAMI SUNSET"));
        manualTracksToBeAdded.add(new Track("MP-ad9", "MT-cee", "MT-cee", "THE END ZONE"));
        manualTracksToBeAdded.add(new Track("MP-ad9", "MT-9c8", "MT-9c8", "DRL 2016"));
        manualTracksToBeAdded.add(new Track("MP-bf7", "MT-f7c", "MT-f7c", "DRL 2016"));
        manualTracksToBeAdded.add(new Track("MP-95a", "MT-9eb", "MT-9eb", "CITY"));
        manualTracksToBeAdded.add(new Track("MP-95a", "MT-6b7", "MT-6b7", "SHIPYARD"));
        manualTracksToBeAdded.add(new Track("MP-95a", "MT-964", "MT-964", "DRL 2016"));
        manualTracksToBeAdded.add(new Track("MP-95a", "MT-46e", "MT-46e", "WOODS"));
        manualTracksToBeAdded.add(new Track("MP-23c", "MT-513", "MT-513", "DRL")); // sandbox
        manualTracksToBeAdded.add(new Track("MP-23c", "MT-fb1", "MT-fb1", "STRAIGHT LINE"));
    }

    public final String token;
    public final String mapsEndpoint;
    private final Duration durationBetweenRequests;
    private final TracksRepository tracksRepository;
    private final LeaderboardRepository leaderboardRepository;

    public TracksDataUpdater(
            @Value("${app.drl-api.token}") String token,
            @Value("${app.drl-api.maps-endpoint}") String mapsEndpoint,
            @Value("${app.drl-api.duration-between-requests}") Duration durationBetweenRequests,
            TracksRepository tracksRepository,
            LeaderboardRepository leaderboardRepository
    ) {
        this.token = token;
        this.mapsEndpoint = mapsEndpoint;
        this.durationBetweenRequests = durationBetweenRequests;
        this.tracksRepository = tracksRepository;
        this.leaderboardRepository = leaderboardRepository;
    }

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    @Order(200)
    public void initialize() {
        long count = tracksRepository.count();
        if(count <= 0) {
            LOG.info("No tracks found in database, initializing...");
            updateMapsData();
        }
    }

    @Transactional
    @Scheduled(cron = "${app.data-updater.tracks.cron}")
    public void updateMapsData() {
        LOG.info("Updating maps data");
        RestTemplate restTemplate = new RestTemplate();
        int pageCount = 1;
        String nextPageUrl = null;
        try {
            do {
                LOG.info("Requesting page " + pageCount);
                UriComponentsBuilder mapsEndpointBuilder = UriComponentsBuilder.fromUriString(mapsEndpoint);
                String requestUrl = mapsEndpointBuilder.buildAndExpand(Map.of("token", token, "page", pageCount)).toUriString();
                LOG.info("requestUrl: " + requestUrl);
                try {
                    Map<String, Object> response = restTemplate.getForObject(
                            requestUrl,
                            Map.class
                    );
                    requestUrl = null;

                    // Check if the API call was successful
                    if ((boolean) response.get("success")) {
                        // Get the data from the response
                        Map<String, Object> data = (Map<String, Object>) response.get("data");
                        Map<String, Object> paging = (Map<String, Object>) data.get("pagging");
                        List<Map<String, Object>> mapsData = (List<Map<String, Object>>) data.get("data");

                        // Loop through the data and create a MapDto for each entry
                        for (Map<String, Object> entry : mapsData) {
                            if (excludedMapsByName.contains(((String) entry.get("map-title")).trim().toUpperCase())) {
                                continue;
                            }
                            Optional<Track> existingTrack = tracksRepository.findByGuid((String) entry.get("guid"));
                            if (existingTrack.isPresent()) {
                                LOG.info("Track already exists: " + existingTrack.get().getGuid());
                            } else {
                                LOG.info("New Track: " + entry.get("guid"));
                            }
                            Track track = existingTrack.orElseGet(Track::new);
                            track.setGuid((String) entry.get("guid"));
                            track.setName(((String) entry.get("map-title")).trim().toUpperCase());
                            track.setDrlTrackId((String) entry.get("track-id"));
                            track.setMapId((String) entry.get("map-id"));
                            track.setMapCategory((String) entry.get("map-category"));
                            if (mappableMapCategories.contains(track.getMapCategory())) {
                                track.setParentCategory(mapCategoryToParentCategory.get(track.getMapCategory()));
                            } else if (mapIdToParentCategory.containsKey(track.getMapId())) {
                                track.setParentCategory(mapIdToParentCategory.get(track.getMapId()));
                            }
                            track.setMapName(mapIdToMapName.get(track.getMapId()));
                            track.setIsDrlOfficial((Boolean) entry.get("is-drl-official"));
                            track.setIsPublic((Boolean) entry.get("is-public"));
                            track.setMapDifficulty((Integer) entry.get("map-difficulty"));
                            track.setMapLaps((Integer) entry.get("map-laps"));
                            if (entry.get("map-distance") instanceof Double) {
                                track.setMapDistance((Double) entry.get("map-distance"));
                            } else if (entry.get("map-distance") instanceof Integer) {
                                track.setMapDistance(Double.valueOf((Integer) entry.get("map-distance")));
                            } else if (entry.get("map-distance") instanceof Long) {
                                track.setMapDistance(Double.valueOf((Long) entry.get("map-distance")));
                            }
                            List<String> categories = (List<String>) entry.get("categories");
                            if (categories.size() > 0) {
                                track.setCategories(categories.get(0));
                            }

                            tracksRepository.save(track);
                        }
                        nextPageUrl = (String) paging.get("next-page-url");
                    }
                } catch (HttpServerErrorException.InternalServerError e) {
                    LOG.error("Error while requesting page " + pageCount + ": " + e.getMessage());
                    LOG.error("requestUrl: " + requestUrl);
                    e.printStackTrace();
                }
                pageCount++;
                Thread.sleep(durationBetweenRequests.toMillis());
            } while (nextPageUrl != null);

            for (Track track : manualTracksToBeAdded) {
                Optional<Track> existingTrack = tracksRepository.findByGuid(track.getGuid());
                if (existingTrack.isPresent()) {
                    track.setId(existingTrack.get().getId());
                    LOG.info("Manual track already exists, overwriting: " + track.getName() + " old id: + " + existingTrack.get().getId() + " new id: " + track.getId());
                    LOG.info(track.toString());
                    tracksRepository.save(track);
                }
                if (mapIdToParentCategory.containsKey(track.getMapId())) {
                    track.setParentCategory(mapIdToParentCategory.get(track.getMapId()));
                }
                track.setMapName(mapIdToMapName.get(track.getMapId()));
                tracksRepository.save(track);
            }
        } catch (Exception e) {
            LOG.error("Error updating maps data", e);
        }
    }
}

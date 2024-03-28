package de.gregord.drlleaderboardbackend.dataupdater;

import de.gregord.drlleaderboardbackend.entities.MapCategory;
import de.gregord.drlleaderboardbackend.entities.Track;
import de.gregord.drlleaderboardbackend.services.DRLApiService;
import de.gregord.drlleaderboardbackend.services.TracksService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import java.util.*;

import static de.gregord.drlleaderboardbackend.config.CacheConfig.CACHE_TRACKS;

@Component
public class TracksOfficialDataUpdater extends TracksDataUpdater {
    public static Logger LOG = LoggerFactory.getLogger(TracksOfficialDataUpdater.class);

    private static final List<String> excludedMapsByName = List.of(
            "FREESTYLE"
    );

    private static final Map<String, MapCategory> mapIdToParentCategory = new HashMap<>();
    private static final Map<String, MapCategory> mapCategoryToParentCategory = new HashMap<>();
    // MapDRL not in here because it consists of DRl Maps/Tracks and Original Maps/Tracks and is handled via mapIdToParentCategory
    private static final Set<String> mappableMapCategories = Set.of(
            "MapMultiGP",
            "MapDRLSimCup",
            "MapFeatured",
            "MapVirtualSeason"
    );

    private static final List<Track> manualTracksToBeAdded = new ArrayList<>();

    static {
        mapIdToParentCategory.put("MP-3fd", MapCategory.MapDRL);
        mapIdToParentCategory.put("MP-693", MapCategory.MapDRL);
        mapIdToParentCategory.put("MP-2e1", MapCategory.MapDRL);
        mapIdToParentCategory.put("MP-3c7", MapCategory.MapDRL);
        mapIdToParentCategory.put("MP-8e5", MapCategory.MapDRL);
        mapIdToParentCategory.put("MP-0a3", MapCategory.MapDRL);
        mapIdToParentCategory.put("MP-639", MapCategory.MapDRL);
        mapIdToParentCategory.put("MP-46c", MapCategory.MapDRL);
        mapIdToParentCategory.put("MP-342", MapCategory.MapDRL);
        mapIdToParentCategory.put("MP-669", MapCategory.MapDRL);
        mapIdToParentCategory.put("MP-0b3", MapCategory.MapDRL);
        mapIdToParentCategory.put("MP-df5", MapCategory.MapDRL);
        mapIdToParentCategory.put("MP-ed5", MapCategory.MapDRL);
        mapIdToParentCategory.put("MP-7ed", MapCategory.MapDRL);
        mapIdToParentCategory.put("MP-a35", MapCategory.MapDRL);
        mapIdToParentCategory.put("MP-92e", MapCategory.MapDRL);
        mapIdToParentCategory.put("MP-ad9", MapCategory.MapDRL);
        mapIdToParentCategory.put("MP-bf7", MapCategory.MapDRL);
        mapIdToParentCategory.put("MP-cb7", MapCategory.MapDRL);

        mapIdToParentCategory.put("MP-2cb", MapCategory.MapOriginal);
        mapIdToParentCategory.put("MP-f95", MapCategory.MapOriginal);
        mapIdToParentCategory.put("MP-19c", MapCategory.MapOriginal);
        mapIdToParentCategory.put("MP-95a", MapCategory.MapOriginal);
        mapIdToParentCategory.put("MP-50c", MapCategory.MapOriginal);
        mapIdToParentCategory.put("MP-615", MapCategory.MapOriginal);
        mapIdToParentCategory.put("MP-103", MapCategory.MapOriginal);
        mapIdToParentCategory.put("MP-409", MapCategory.MapOriginal);
        mapIdToParentCategory.put("MP-23c", MapCategory.MapOriginal);
        mapIdToParentCategory.put("MP-b59", MapCategory.MapOriginal);

        mapIdToParentCategory.put("MP-0c6", MapCategory.MapMultiGP);

        mapCategoryToParentCategory.put("MapMultiGP", MapCategory.MapMultiGP);
        mapCategoryToParentCategory.put("MapDRLSimCup", MapCategory.MapDRLSimCup);
        mapCategoryToParentCategory.put("MapFeatured", MapCategory.MapFeatured);
        mapCategoryToParentCategory.put("MapVirtualSeason", MapCategory.MapVirtualSeason);

        manualTracksToBeAdded.add(new Track("MP-ed5", "MT-418", "MT-418", "DRL 2016", MapCategory.MapDRL));
        manualTracksToBeAdded.add(new Track("MP-a35", "MT-a72", "MT-a72", "CONFIRM NOR DENY", MapCategory.MapDRL));
        manualTracksToBeAdded.add(new Track("MP-a35", "MT-3dd", "MT-3dd", "RESTRICTED AREA", MapCategory.MapDRL));
        manualTracksToBeAdded.add(new Track("MP-a35", "MT-1e7", "MT-1e7", "DRL 2016", MapCategory.MapDRL));
        manualTracksToBeAdded.add(new Track("MP-92e", "MT-2a7", "MT-2a7", "PROJECT NOOB", MapCategory.MapDRL));
        manualTracksToBeAdded.add(new Track("MP-92e", "MT-39f", "MT-39f", "THE TRINITY TEST", MapCategory.MapDRL));
        manualTracksToBeAdded.add(new Track("MP-92e", "MT-5db", "MT-5db", "DRL 2016", MapCategory.MapDRL));
        manualTracksToBeAdded.add(new Track("MP-ad9", "MT-ccd", "MT-ccd", "MIAMI SUNSET", MapCategory.MapDRL));
        manualTracksToBeAdded.add(new Track("MP-ad9", "MT-cee", "MT-cee", "THE END ZONE", MapCategory.MapDRL));
        manualTracksToBeAdded.add(new Track("MP-ad9", "MT-9c8", "MT-9c8", "DRL 2016", MapCategory.MapDRL));
        manualTracksToBeAdded.add(new Track("MP-bf7", "MT-f7c", "MT-f7c", "DRL 2016", MapCategory.MapDRL));
        manualTracksToBeAdded.add(new Track("MP-95a", "MT-9eb", "MT-9eb", "CITY", MapCategory.MapDRL));
        manualTracksToBeAdded.add(new Track("MP-95a", "MT-6b7", "MT-6b7", "SHIPYARD", MapCategory.MapDRL));
        manualTracksToBeAdded.add(new Track("MP-95a", "MT-964", "MT-964", "DRL 2016", MapCategory.MapDRL));
        manualTracksToBeAdded.add(new Track("MP-95a", "MT-46e", "MT-46e", "WOODS", MapCategory.MapDRL));
        manualTracksToBeAdded.add(new Track("MP-23c", "MT-513", "MT-513", "DRL", MapCategory.MapDRL));
        manualTracksToBeAdded.add(new Track("MP-23c", "MT-fb1", "MT-fb1", "STRAIGHT LINE", MapCategory.MapDRL));
        manualTracksToBeAdded.add(new Track("MP-ad9", "CMP-501cb857a4c399073d33d324", "MT-a09", "CTG / 07: TREEHOUSE TANGO",
                MapCategory.MapFeatured));
    }

    public TracksOfficialDataUpdater(
            @Value("${app.drl-api.token}") String token,
            @Value("${app.drl-api.maps-endpoint}") String mapsEndpoint,
            DRLApiService drlApiService,
            TracksService tracksService,
            CacheManager cacheManager
    ) {
        super(
                token,
                mapsEndpoint,
                drlApiService,
                tracksService,
                cacheManager,
                mappableMapCategories,
                mapCategoryToParentCategory,
                MapCategory.getOfficialCategoriesIds()
        );
        super.setExcludedMapsByName(excludedMapsByName);
        super.setMapIdToParentCategory(mapIdToParentCategory);
        super.setManualTracksToBeAdded(manualTracksToBeAdded);
    }

    @Override
    @CacheEvict(value = CACHE_TRACKS, allEntries = true)
    public void initialize() {
        super.initialize();
    }

    @Override
    @CacheEvict(value = CACHE_TRACKS, allEntries = true)
    public void updateMapsData() {
        super.updateMapsData();
    }
}

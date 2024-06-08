package de.gregord.drlleaderboardbackend.dataupdater;

import de.gregord.drlleaderboardbackend.entities.MapCategory;
import de.gregord.drlleaderboardbackend.services.DRLApiService;
import de.gregord.drlleaderboardbackend.services.TracksService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static de.gregord.drlleaderboardbackend.config.CacheConfig.CACHE_COMMUNITY_TRACKS;

@Component
public class TracksCommunityDataUpdater extends TracksDataUpdater {
    private static final List<String> excludedMapsByName = List.of(
//            "CTG / 07: TREEHOUSE TANGO" // this is in the official section despite being flagged as a community track
    );
    private static final Map<String, MapCategory> mapCategoryToParentCategory = new HashMap<>();
    private static final Set<String> mappableMapCategories = Set.of(
            "MapCommon"
    );

    static {
        mapCategoryToParentCategory.put("MapCommon", MapCategory.MapCommunity);
    }

    public TracksCommunityDataUpdater(
            @Value("${app.drl-api.token}") String token,
            @Value("${app.drl-api.community-maps-endpoint}") String mapsEndpoint,
            DRLApiService drlApiService,
            TracksService tracksService,
            CacheManager cacheManager
    ) {
        super(token, mapsEndpoint, drlApiService, tracksService, cacheManager, mappableMapCategories,
                mapCategoryToParentCategory, MapCategory.getCommunityCategoriesIds());
        super.setExcludedMapsByName(excludedMapsByName);
        setPageLimit(10);
    }

    @Override
    @CacheEvict(value = CACHE_COMMUNITY_TRACKS, allEntries = true)
    public void initialize() {
        super.initialize();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_COMMUNITY_TRACKS, allEntries = true),
    })
    public void updateMapsData() {
        super.updateMapsData();
    }
}

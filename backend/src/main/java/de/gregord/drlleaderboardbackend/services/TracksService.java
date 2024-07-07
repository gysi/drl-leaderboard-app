package de.gregord.drlleaderboardbackend.services;

import de.gregord.drlleaderboardbackend.domain.Season;
import de.gregord.drlleaderboardbackend.domain.TrackCommunityView;
import de.gregord.drlleaderboardbackend.domain.TrackView;
import de.gregord.drlleaderboardbackend.entities.MapCategory;
import de.gregord.drlleaderboardbackend.entities.Track;
import de.gregord.drlleaderboardbackend.repositories.TracksRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static de.gregord.drlleaderboardbackend.config.CacheConfig.CACHE_INTERNAL_MAPID_TRACKID_MAPPING;

@Service
@Transactional(readOnly = true)
public class TracksService {
    private static final Logger LOG = LoggerFactory.getLogger(TracksService.class);
    private final TracksRepository tracksRepository;
    private final LeaderboardService leaderboardService;

    public TracksService(TracksRepository tracksRepository,
                         LeaderboardService leaderboardService){
        this.tracksRepository = tracksRepository;
        this.leaderboardService = leaderboardService;
    }

    public Collection<TrackCommunityView> getTracksBySeason(Season season){
        return getTracksBySeason(season.getSeasonId());
    }

    public Collection<TrackCommunityView> getTracksBySeason(int seasonId){
        return tracksRepository.findTracksBySeasonId(seasonId);
    }

    // TODO solve this by using the new community_seasons table
//    public Collection<Track> getCommunityTracksForCurrentSeason() {
//        return tracksRepository.findByCommunitySeasonAndMapCategoryId(
//                Season.getCurrentSeasonId(),
//                MapCategory.MapCommon.ordinal()
//        );
//    }

    /**
     * The track id for a certain mapId is always the same. The trackId can only be read from the leaderboard entries
     * and since this is too expensive to determine for every map we can use this map.
     * @return Map<String, String> Key is MapId and Value is TrackId
     */
    @Cacheable(value = CACHE_INTERNAL_MAPID_TRACKID_MAPPING, key = "'"+CACHE_INTERNAL_MAPID_TRACKID_MAPPING+"'")
    public Map<String, String> getDrlTrackIdByMapIdMap(){
        return tracksRepository.findDistinctMapIdAndDrlTrackId()
                .stream()
                .collect(Collectors.toMap(MapIdAndTrackIdView::getMapId, MapIdAndTrackIdView::getDrlTrackId));
    }

    public interface MapIdAndTrackIdView {
        String getMapId();
        String getDrlTrackId();
    }

    public Optional<Track> findById(Long trackId) {
        return tracksRepository.findById(trackId);
    }

    public long countByMapCategoryIdIn(Set<Integer> mapCategoryIds) {
        return tracksRepository.countByMapCategoryIdIn(mapCategoryIds);
    }

    public Collection<Track> findByGuidIn(Collection<String> guids) {
        return tracksRepository.findByGuidIn(guids);
    }

    public Optional<Track> findByGuid(String guid) {
        return tracksRepository.findByGuid(guid);
    }

    @Transactional
    public void saveAllAndDeleteMissing(Collection<Track> tracksToBeSaved, Set<Integer> mapCategoryIds, boolean preventDeletion){
        List<Track> tracks = tracksRepository.saveAll(tracksToBeSaved.stream().distinct().collect(Collectors.toList()));

        // Only run deletion if no error occurred, otherwise we might delete tracks that are still active
        if(!preventDeletion) {
            Collection<Track> byTrackNotIn = tracksRepository.findByIdNotInAndMapCategoryIdIn(
                    tracks.stream().map(Track::getId).collect(Collectors.toList()),
                    mapCategoryIds
            );
            LOG.info("Deleting " + byTrackNotIn.size() + " tracks");
            LOG.info(byTrackNotIn.stream().map(Track::getName).collect(Collectors.joining(", ")));
            byTrackNotIn.forEach(track -> leaderboardService.findByTrackId(track.getId()).forEach(entry -> {
                leaderboardService.deleteBeatenByEntriesByLeaderboardId(entry.getId());
                leaderboardService.delete(entry);
            }));
            tracksRepository.deleteAll(byTrackNotIn);
        }
    }
}

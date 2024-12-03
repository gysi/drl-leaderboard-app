package de.gregord.drlleaderboardbackend.controllers;

import de.gregord.drlleaderboardbackend.domain.LeaderboardByPlayerView;
import de.gregord.drlleaderboardbackend.domain.Season;
import de.gregord.drlleaderboardbackend.domain.TrackCommunitySeasonView;
import de.gregord.drlleaderboardbackend.domain.TrackView;
import de.gregord.drlleaderboardbackend.entities.MapCategory;
import de.gregord.drlleaderboardbackend.repositories.TracksRepository;
import de.gregord.drlleaderboardbackend.services.CommunitySeasonService;
import de.gregord.drlleaderboardbackend.services.DRLApiService;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/tracks")
public class TrackController {
    private final TracksRepository tracksRepository;
    private final DRLApiService drlApiService;
    private final CommunitySeasonService communitySeasonService;

    public TrackController(
            TracksRepository tracksRepository,
            DRLApiService drlApiService,
            CommunitySeasonService communitySeasonService
    ) {
        this.tracksRepository = tracksRepository;
        this.drlApiService = drlApiService;
        this.communitySeasonService = communitySeasonService;
    }

    @GetMapping()
    public ResponseEntity<List<TrackView>> tracks() {
        List<TrackView> all = tracksRepository.getAllActiveTracks(
                MapCategory.getOfficialCategoriesIds(),
                Season.getCurrentSeasonId(),
                Sort.by(Sort.Order.asc("mapName"), Sort.Order.asc("parentCategory"), Sort.Order.asc("name"))
        );
        return ResponseEntity.ok(all);
    }

    @GetMapping("/community-season")
    public ResponseEntity<List<TrackCommunitySeasonView>> communitySeasonTracks(@RequestParam String seasionIdName) {
        Season bySeasionIdName = Season.getBySeasonIdName(seasionIdName);
        if (bySeasionIdName == null) {
            return ResponseEntity.notFound().build();
        }
        List<TrackCommunitySeasonView> all = communitySeasonService.findBySeasonIdAndExcludedIsFalseAndSortedByDifficulty(
                bySeasionIdName.getId());
        return ResponseEntity.ok(all);
    }

    @GetMapping("/community-season/current")
    public ResponseEntity<List<TrackCommunitySeasonView>> currentCommunitySeasonTracks() {
        return communitySeasonTracks(Season.getCurrentSeasonIdName());
    }

    @GetMapping("/community-season/current/missing-tracks-by-playername")
    public ResponseEntity<List<LeaderboardByPlayerView.LeaderboardByPlayerView_Track>> missingTracksForCurrentSeasonByPlayerName(
            @RequestParam String playerName) {
        List<LeaderboardByPlayerView.LeaderboardByPlayerView_Track> missingTracks =
                tracksRepository.findMissingTracksForCurrentSeasonByPlayerName(Season.getCurrentSeasonId(), playerName);
        return ResponseEntity.ok(missingTracks);
    }

    @GetMapping("/parent-categories")
    public ResponseEntity<Set<MapCategory>> parentCategories() {
        return ResponseEntity.ok(MapCategory.getOfficialCategories());
    }

    @GetMapping("/missing-official-tracks-by-playername")
    public ResponseEntity<List<LeaderboardByPlayerView.LeaderboardByPlayerView_Track>> missingTracksByPlayerName(
            @RequestParam String playerName) {
        List<LeaderboardByPlayerView.LeaderboardByPlayerView_Track> missingTracks =
                tracksRepository.findMissingTracksByPlayerName(MapCategory.getOfficialCategoriesIds(), playerName);
        return ResponseEntity.ok(missingTracks);
    }

    @GetMapping("/details/{trackId}")
    public ResponseEntity<LinkedHashMap<String, Object>> getMapDetails(@PathVariable String trackId) {
        LinkedHashMap<String, Object> mapDetails = drlApiService.getMapDetails(trackId);
        return ResponseEntity.ok(mapDetails);
    }
}

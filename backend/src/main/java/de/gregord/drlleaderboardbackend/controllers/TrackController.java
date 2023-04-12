package de.gregord.drlleaderboardbackend.controllers;

import de.gregord.drlleaderboardbackend.domain.LeaderboardByPlayerView;
import de.gregord.drlleaderboardbackend.domain.TrackView;
import de.gregord.drlleaderboardbackend.repositories.TracksRepository;
import de.gregord.drlleaderboardbackend.services.DRLApiService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/tracks")
public class TrackController {

    private final TracksRepository tracksRepository;
    private final DRLApiService drlApiService;
    private final ModelMapper modelMapper;

    public TrackController(
            ModelMapper modelMapper,
            TracksRepository tracksRepository,
            DRLApiService drlApiService
    ) {
        this.modelMapper = modelMapper;
        this.tracksRepository = tracksRepository;
        this.drlApiService = drlApiService;
    }

    @GetMapping
    public ResponseEntity<List<TrackView>> tracks() {
        List<TrackView> all = tracksRepository.findBy(
                Sort.by(Sort.Order.asc("mapName"), Sort.Order.asc("parentCategory"), Sort.Order.asc("name")),
                TrackView.class
        );
        return ResponseEntity.ok(all);
    }

    @GetMapping("/parent-categories")
    public ResponseEntity<List<String>> parentCategories() {
        List<String> parentCategories = tracksRepository.findDistinctByParentCategory();
        return ResponseEntity.ok(parentCategories);
    }

    @GetMapping("/missing-tracks-by-playername")
    public ResponseEntity<List<LeaderboardByPlayerView.LeaderboardByPlayerView_Track>> missingTracksByPlayerName(@RequestParam String playerName) {
        List<LeaderboardByPlayerView.LeaderboardByPlayerView_Track> players = tracksRepository.findMissingTracksByPlayerName(playerName);
        return ResponseEntity.ok(players);
    }

    @GetMapping("/details/{trackId}")
    public ResponseEntity<LinkedHashMap> getMapDetails(@PathVariable String trackId) {
        LinkedHashMap mapDetails = drlApiService.getMapDetails(trackId);
        return ResponseEntity.ok(mapDetails);
    }
}

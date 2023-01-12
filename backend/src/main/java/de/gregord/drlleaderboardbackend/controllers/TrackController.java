package de.gregord.drlleaderboardbackend.controllers;

import de.gregord.drlleaderboardbackend.domain.LeaderboardByPlayerView;
import de.gregord.drlleaderboardbackend.domain.TrackView;
import de.gregord.drlleaderboardbackend.repositories.TracksRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tracks")
public class TrackController {

    private final TracksRepository tracksRepository;
    private final ModelMapper modelMapper;

    public TrackController(
            ModelMapper modelMapper,
            TracksRepository tracksRepository
    ) {
        this.modelMapper = modelMapper;
        this.tracksRepository = tracksRepository;
    }

    @GetMapping
    public ResponseEntity<List<TrackView>> tracks() {
        List<TrackView> all = tracksRepository.findBy(
                Sort.by(Sort.Order.asc("mapName"), Sort.Order.asc("parentCategory"), Sort.Order.asc("name")),
                TrackView.class
        );
        return ResponseEntity.ok(all);
    }

    @GetMapping("/missingtracksbyplayername")
    public ResponseEntity<List<LeaderboardByPlayerView.LeaderboardByPlayerView_Track>> missingTracksByPlayerName(@RequestParam String playerName) {
        List<LeaderboardByPlayerView.LeaderboardByPlayerView_Track> players = tracksRepository.findMissingTracksByPlayerName(playerName);
        return ResponseEntity.ok(players);
    }
}

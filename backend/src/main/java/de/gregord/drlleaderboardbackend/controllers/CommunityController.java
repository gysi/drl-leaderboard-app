package de.gregord.drlleaderboardbackend.controllers;

import de.gregord.drlleaderboardbackend.domain.Season;
import de.gregord.drlleaderboardbackend.domain.TrackCommunityView;
import de.gregord.drlleaderboardbackend.services.TracksService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/community")
public class CommunityController {

    private final TracksService tracksService;

    public CommunityController(TracksService tracksService) {
        this.tracksService = tracksService;
    }

    @GetMapping("/tracks")
    public ResponseEntity<Collection<TrackCommunityView>> getTracks(@RequestParam String seasonIdName) {
        Season bySeasionIdName = Season.getBySeasionIdName(seasonIdName);
        if (bySeasionIdName == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tracksService.getTracksBySeason(bySeasionIdName));
    }

    @GetMapping("/tracks-current-season")
    public ResponseEntity<Collection<TrackCommunityView>> getTracksCurrentSeason() {
        return ResponseEntity.ok(tracksService.getTracksBySeason(Season.getCurrentSeason()));
    }
}

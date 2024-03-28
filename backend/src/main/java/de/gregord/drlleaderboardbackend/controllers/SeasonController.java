package de.gregord.drlleaderboardbackend.controllers;

import de.gregord.drlleaderboardbackend.domain.CommunityRankingView;
import de.gregord.drlleaderboardbackend.domain.Season;
import de.gregord.drlleaderboardbackend.services.CommunitySeasonService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seasons")
public class SeasonController {

    private final CommunitySeasonService communitySeasonService;

    public SeasonController(CommunitySeasonService communitySeasonService) {
        this.communitySeasonService = communitySeasonService;
    }

    @GetMapping
    public ResponseEntity<Season[]> seasons() {
        return ResponseEntity.ok(Season.values());
    }

    @GetMapping("/current")
    public ResponseEntity<Season> currentSeason() {
        return ResponseEntity.ok(Season.getCurrentSeason());
    }

    @GetMapping("/next")
    public ResponseEntity<Season> nextSeason() {
        return ResponseEntity.ok(Season.getNextSeason());
    }

    @GetMapping("/ranking-current-season")
    public ResponseEntity<List<CommunityRankingView>> ranking(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "50") @Min(1) @Max(500) int limit
    ) {
        List<CommunityRankingView> overallRanking = communitySeasonService.getOverallRankingCurrentSeason(page, limit);
        return ResponseEntity.ok(overallRanking);
    }
}

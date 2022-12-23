package de.gregord.drlleaderboardbackend.controllers;


import de.gregord.drlleaderboardbackend.domain.LeaderBoardByTrackView;
import de.gregord.drlleaderboardbackend.domain.LeaderboardByPlayerView;
import de.gregord.drlleaderboardbackend.domain.OverallRankingView;
import de.gregord.drlleaderboardbackend.repositories.LeaderboardRepository;
import de.gregord.drlleaderboardbackend.services.LeaderboardService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaderboards")
public class LeaderboardController {
    private final LeaderboardRepository leaderboardRepository;
    private final LeaderboardService leaderboardService;

    public LeaderboardController(
            LeaderboardRepository leaderboardRepository,
            LeaderboardService leaderboardService
    ) {
        this.leaderboardRepository = leaderboardRepository;
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("/byplayername/{playerName}")
    public ResponseEntity<List<LeaderboardByPlayerView>> getLeaderboardByPlayerName(@PathVariable String playerName) {
        Sort sorting = Sort.by(
                Sort.Order.asc("track.mapName"),
                Sort.Order.asc("track.parentCategory"),
                Sort.Order.asc("track.name"),
                Sort.Order.desc("beatenBy.createdAt")
        );
        List<LeaderboardByPlayerView> byPlayerName = leaderboardRepository.findByPlayerName(playerName, sorting);
        return ResponseEntity.ok(byPlayerName);
    }

    @GetMapping("/overallranking")
    public ResponseEntity<List<OverallRankingView>> getOverallRanking(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int limit
    ) throws Exception {
        List<OverallRankingView> overallRanking = leaderboardService.getOverallRanking(page, limit);
        return ResponseEntity.ok(overallRanking);
    }

    @GetMapping("/bytrack/{trackId}")
    public ResponseEntity<List<LeaderBoardByTrackView>> getLeaderboardByPlayerName(
            @PathVariable Long trackId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int limit
    ) {
        List<LeaderBoardByTrackView> byGuid = leaderboardRepository.findByTrackId(
                trackId,
                PageRequest.of(page - 1, limit)
                        .withSort(Sort.by(Sort.Order.asc("position")))
        );
        return ResponseEntity.ok(byGuid);
    }
}

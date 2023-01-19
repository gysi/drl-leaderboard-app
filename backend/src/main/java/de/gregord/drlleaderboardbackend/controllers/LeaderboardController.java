package de.gregord.drlleaderboardbackend.controllers;


import de.gregord.drlleaderboardbackend.domain.*;
import de.gregord.drlleaderboardbackend.repositories.LeaderboardRepository;
import de.gregord.drlleaderboardbackend.services.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
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

    @Autowired
    CacheManager cacheManager;

    public LeaderboardController(
            LeaderboardRepository leaderboardRepository,
            LeaderboardService leaderboardService
    ) {
        this.leaderboardRepository = leaderboardRepository;
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("/byplayername")
    public ResponseEntity<List<LeaderboardByPlayerView>> getLeaderboardByPlayerName(@RequestParam String playerName) {
        Sort sorting = Sort.by(
                Sort.Order.asc("track.mapName"),
                Sort.Order.asc("track.parentCategory"),
                Sort.Order.asc("track.name"),
                Sort.Order.desc("createdAt"),
                Sort.Order.desc("beatenBy.createdAt")
        );
        List<LeaderboardByPlayerView> byPlayerName = leaderboardRepository.findByPlayerName(playerName, sorting);
//        CacheInfo cacheInfo = CacheInfo.of(((SpringCache2kCache) cacheManager.getCache("leaderboardbyplayername")).getNativeCache());
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
                        .withSort(Sort.by(Sort.Order.asc("position"), Sort.Order.asc("score")))
        );
        return ResponseEntity.ok(byGuid);
    }

    @GetMapping("/findPlayers")
    public ResponseEntity<List<String>> findPlayers(@RequestParam String playerName) {
        List<String> players = leaderboardRepository
                .findDistinctPlayerNames(
                        playerName.toLowerCase().trim() + '%',
                        PageRequest.of(0, 50)
                                .withSort(Sort.by(Sort.Order.asc("playerName")))
                );
        return ResponseEntity.ok(players);
    }

    @GetMapping("/latestActivity")
    public ResponseEntity<List<LeaderboardActivityView>> latestActivity() {
        List<LeaderboardActivityView> players = leaderboardRepository.latestLeaderboardActivity();
        return ResponseEntity.ok(players);
    }

    @GetMapping("/latestActivityTop10")
    public ResponseEntity<List<LeaderboardActivityView>> latestActivityTop10() {
        List<LeaderboardActivityView> players = leaderboardRepository.latestLeaderboardActivityTop10();
        return ResponseEntity.ok(players);
    }

    @GetMapping("/mostPbsLast7Days")
    public ResponseEntity<List<LeaderboardMostPbsView>> mostPbsLast7Days() {
        List<LeaderboardMostPbsView> players = leaderboardRepository.mostPbsLast7Days();
        return ResponseEntity.ok(players);
    }

    @GetMapping("/mostPbsLastMonth")
    public ResponseEntity<List<LeaderboardMostPbsView>> mostPbsLastMonth() {
        List<LeaderboardMostPbsView> players = leaderboardRepository.mostPbsLastMonth();
        return ResponseEntity.ok(players);
    }
}

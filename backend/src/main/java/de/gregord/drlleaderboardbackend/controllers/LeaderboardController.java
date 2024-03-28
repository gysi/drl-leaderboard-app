package de.gregord.drlleaderboardbackend.controllers;


import de.gregord.drlleaderboardbackend.domain.*;
import de.gregord.drlleaderboardbackend.entities.MapCategory;
import de.gregord.drlleaderboardbackend.repositories.LeaderboardRepository;
import de.gregord.drlleaderboardbackend.services.LeaderboardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @GetMapping("/official/byplayername")
    public ResponseEntity<List<LeaderboardByPlayerView>> getLeaderboardByPlayerName(@RequestParam String playerName) {
        List<LeaderboardByPlayerView> byPlayerName =
                leaderboardService.getOfficialTrackLeaderboardForPlayer(playerName);
//        CacheInfo cacheInfo = CacheInfo.of(((SpringCache2kCache) cacheManager.getCache("leaderboardbyplayername")).getNativeCache());
        return ResponseEntity.ok(byPlayerName);
    }

    @GetMapping("/community-season/by-playername/current-seasons")
    public ResponseEntity<List<LeaderboardByPlayerView>> getLeaderboardByPlayerNameForCurrentSeason(
            @RequestParam String playerName) {
        List<LeaderboardByPlayerView> byPlayerName =
                leaderboardService.getTrackLeaderboardForPlayerForCurrentSeason(playerName);
        return ResponseEntity.ok(byPlayerName);
    }

    @GetMapping("/official/overall-ranking")
    public ResponseEntity<List<OverallRankingView>> getOverallRanking(
            @RequestParam(required = false) Optional<String> parentCategory,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "50") @Min(1) @Max(500) int limit
    ) {
        List<OverallRankingView> overallRanking = leaderboardService.getOverallRanking(
                parentCategory.flatMap(MapCategory::fromString).map(p -> Set.of(p.ordinal())).orElse(MapCategory.getOfficialCategoriesIds()),
                page,
                limit);
        return ResponseEntity.ok(overallRanking);
    }

    @GetMapping("/bytrack/{trackId}")
    public ResponseEntity<List<LeaderboardByTrackView>> getLeaderboardByTrackId(
            @PathVariable Long trackId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int limit
    ) {
        List<LeaderboardByTrackView> byGuid = leaderboardRepository.findByTrackId(
                trackId,
                PageRequest.of(page - 1, limit)
                        .withSort(Sort.by(Sort.Order.asc("position"), Sort.Order.asc("score")))
        );
        return ResponseEntity.ok(byGuid);
    }

    @GetMapping("/replays/bytrack/{trackId}")
    public ResponseEntity<List<ReplaysByTrackView>> getReplaysByTrackId(
            @PathVariable Long trackId
    ) {
        List<ReplaysByTrackView> replays = leaderboardRepository.findByTrackIdAndIsInvalidRunFalse(trackId, Sort.by(Sort.Order.asc(
                "position")));
        return ResponseEntity.ok(replays);
    }

    @GetMapping("/latest-activity")
    public ResponseEntity<List<LeaderboardActivityView>> latestActivity() {
        List<LeaderboardActivityView> players = leaderboardRepository.latestLeaderboardActivity();
        return ResponseEntity.ok(players);
    }

    @GetMapping("/latest-activity-top-10")
    public ResponseEntity<List<LeaderboardActivityView>> latestActivityTop10() {
        List<LeaderboardActivityView> players = leaderboardRepository.latestLeaderboardActivityTop10();
        return ResponseEntity.ok(players);
    }

    @GetMapping("/most-pbs-last-7-days")
    public ResponseEntity<List<LeaderboardMostPbsView>> mostPbsLast7Days() {
        List<LeaderboardMostPbsView> players = leaderboardRepository.mostPbsLast7Days();
        return ResponseEntity.ok(players);
    }

    @GetMapping("/most-pbs-last-month")
    public ResponseEntity<List<LeaderboardMostPbsView>> mostPbsLastMonth() {
        List<LeaderboardMostPbsView> players = leaderboardRepository.mostPbsLastMonth();
        return ResponseEntity.ok(players);
    }

    @GetMapping("/most-track-entries-last-month")
    public ResponseEntity<List<LeaderboardMostTrackEntriesView>> mostEntriesByTrackLastMonth() {
        List<LeaderboardMostTrackEntriesView> tracks = leaderboardRepository.mostEntriesByTrackLastMonth();
        return ResponseEntity.ok(tracks);
    }

    @GetMapping("/most-track-entries-last-14-days")
    public ResponseEntity<List<LeaderboardMostTrackEntriesView>> mostEntriesByTrackLast14Days() {
        List<LeaderboardMostTrackEntriesView> tracks = leaderboardRepository.mostEntriesByTrackLast14Days();
        return ResponseEntity.ok(tracks);
    }

    @PostMapping("/worst-tracks")
    public ResponseEntity<List<WorstTracksView>> worstTracks(
            @RequestBody @Valid RequestBodyWorstTracks requestBodyWorstTracks
    ) {
        List<WorstTracksView> worstTracksByPlayer = leaderboardRepository.worstTracksByPlayer(
                requestBodyWorstTracks.getPlayerName(),
                requestBodyWorstTracks.getIncludeImprovementIsLongAgo(),
                requestBodyWorstTracks.getIncludeWorstPosition(),
                requestBodyWorstTracks.getIncludeMostBeatenByEntries(),
                requestBodyWorstTracks.getIncludeFarthestBehindLeader(),
                requestBodyWorstTracks.getIncludePotentiallyEasyToAdvance(),
                requestBodyWorstTracks.getIncludeInvalidRuns(),
                requestBodyWorstTracks.getIncludeNotCompleted(),
                Optional.ofNullable(requestBodyWorstTracks.getExcludedTracks()).orElse(List.of()).stream()
                        .filter(s -> s.chars().allMatch(Character::isDigit))
                        .map(Long::parseLong).toList(),
                MapCategory.getOfficialCategoriesIds(),
                Season.getCurrentSeasonId(),
                false // TODO let the user choose to only include community season tracks
        );
        return ResponseEntity.ok(worstTracksByPlayer);
    }
}

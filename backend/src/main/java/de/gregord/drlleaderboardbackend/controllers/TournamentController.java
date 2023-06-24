package de.gregord.drlleaderboardbackend.controllers;

import de.gregord.drlleaderboardbackend.domain.*;
import de.gregord.drlleaderboardbackend.domain.TournamentRankingsOuterClass;
import de.gregord.drlleaderboardbackend.domain.convert.TournamentRankingsToProto;
import de.gregord.drlleaderboardbackend.repositories.TournamentRepository;
import de.gregord.drlleaderboardbackend.services.TournamentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static de.gregord.drlleaderboardbackend.domain.Season.SEASON_MAPPING_BY_DATE;

@RestController
@RequestMapping("/tournaments")
public class TournamentController {

    TournamentService tournamentService;
    TournamentRepository tournamentRepository;

    public TournamentController(TournamentService tournamentService, TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentService = tournamentService;
    }

    @GetMapping("/season-status")
    public TournamentSeason seasonStatus() {
        return tournamentService.getSeasonStatus(SEASON_MAPPING_BY_DATE.floorEntry(LocalDateTime.now()).getValue().getSeasonId());
    }

    @GetMapping("/rankings-for-season")
    public TournamentRankings rankingsForSeason(String seasonId) {
        return tournamentService.getTournamentRankingForSeason(seasonId);
    }

    @GetMapping(path = "/rankings-current-season", produces = {"application/json", "application/x-protobuf"})
    public TournamentRankingsOuterClass.TournamentRankings rankingsCurrentSeason() {
        return TournamentRankingsToProto.convertDomainToProto(tournamentService.getTournamentRankingForSeason(SEASON_MAPPING_BY_DATE.floorEntry(LocalDateTime.now()).getValue().getSeasonId()));
    }

    @GetMapping("/seasons")
    public String[] seasons() {
        //TODO
//        return tournamentService.getSeasons();
        return new String[]{"TODO"};
    }

    @GetMapping("/tournaments-current-season")
    public List<TournamentView> tournamentsCurrentSeason() {
        Season season = SEASON_MAPPING_BY_DATE.floorEntry(LocalDateTime.now()).getValue();
        return tournamentRepository._getTournament(season.getSeasonStartDate(), season.getSeasonEndDate());
    }
}

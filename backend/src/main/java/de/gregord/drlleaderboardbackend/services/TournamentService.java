package de.gregord.drlleaderboardbackend.services;

import de.gregord.drlleaderboardbackend.domain.TournamentRankings;
import de.gregord.drlleaderboardbackend.entities.Tournament;
import de.gregord.drlleaderboardbackend.entities.tournament.TournamentRanking;
import de.gregord.drlleaderboardbackend.repositories.TournamentRepository;
import org.slf4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TournamentService {
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(TournamentService.class);

    public enum Season {
        SEASON_2023_24("2023-24", "Season 2023-24",
                LocalDateTime.of(2024,5,23, 0, 0)),
        SEASON_2024_25("2024-25", "Season 2024-25",
                LocalDateTime.of(2025,5,23, 0, 0));

        private final String seasonId;
        private final String seasonName;
        private final LocalDateTime seasonEndDate;

        Season(String seasonId, String seasonName, LocalDateTime seasonEndDate) {
            this.seasonId = seasonId;
            this.seasonName = seasonName;
            this.seasonEndDate = seasonEndDate;
        }

        public String getSeasonId() {
            return seasonId;
        }

        public String getSeasonName() {
            return seasonName;
        }

        public LocalDateTime getSeasonEndDate() {
            return seasonEndDate;
        }
    }

    TournamentRepository tournamentRepository;

    public TournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @Transactional(readOnly = true)
    @Cacheable("tournamentRankings")
    public TournamentRankings getOverallRanking() {
        try(Stream<Tournament> tournaments = tournamentRepository.streamTournamentsForOverallRanking()) {
            Map<String, TournamentRankings.SeasonRanking> seasonRankingsMap = new java.util.HashMap<>();
            Map<String, Map<String, TournamentRankings.PlayerRanking>> playerRankingsMapBySeason = new java.util.HashMap<>();
            for (Season season : Season.values()) {
                seasonRankingsMap.put(season.getSeasonId(), new TournamentRankings.SeasonRanking(season.getSeasonName()));
                playerRankingsMapBySeason.put(season.getSeasonId(), new java.util.HashMap<>());
            }
            tournaments.forEach(tournament -> {
                String seasonId = getSeasonId(tournament);
                Map<String, TournamentRankings.PlayerRanking> playerRankingMap = playerRankingsMapBySeason.get(seasonId);
                List<TournamentRanking> rankings = tournament.getRankings();
                for (int i = 0; i < rankings.size(); i++) {
                    int position = i+1;
                    if(position > 11){ // 12th place and above get no points
                        break;
                    }
                    TournamentRanking ranking = rankings.get(i);
                    TournamentRankings.PlayerRanking playerRanking = playerRankingMap.computeIfAbsent(ranking.getPlayerId(), playerId -> {
                        TournamentRankings.PlayerRanking newPlayerRanking = new TournamentRankings.PlayerRanking();
                        newPlayerRanking.setCommonPlayerName(ranking.getProfileName());
                        return newPlayerRanking;
                    });
                    playerRanking.setCommonPlayerName(ranking.getProfileName());
                    playerRanking.addPoints(getPointByIRLSystem(position));
                    playerRanking.incrementNumberOfTournamentsPlayed();
                    TournamentRankings.Tournament tournamentPlayerParticipatedIn = new TournamentRankings.Tournament();
                    tournamentPlayerParticipatedIn.setNameUsedInGame(ranking.getProfileName());
                    tournamentPlayerParticipatedIn.setTitle(tournament.getTitle());
                    tournamentPlayerParticipatedIn.setPosition(position);
                    tournamentPlayerParticipatedIn.setStartDate(tournament.getRegistrationEndAt());
                    playerRanking.getTournaments().addFirst(tournamentPlayerParticipatedIn);
                }
            });

            TournamentRankings tournamentRankings = new TournamentRankings();
            List<TournamentRankings.SeasonRanking> seasons = tournamentRankings.getSeasons();
            Arrays.stream(Season.values()).forEach(season -> {
                Map<String, TournamentRankings.PlayerRanking> playerRankingMap = playerRankingsMapBySeason.get(season.seasonId);
                List<TournamentRankings.PlayerRanking> sortedPlayerRankingsForSeason = playerRankingMap.values().stream()
                        .sorted((o1, o2) -> o2.getPoints().compareTo(o1.getPoints()))
                        .collect(Collectors.toList());
                // set most used name as common name for each PlayerRanking
                sortedPlayerRankingsForSeason.forEach(playerRanking -> {
                    String mostUsedName = playerRanking.getTournaments().stream()
                            .collect(Collectors.groupingBy(TournamentRankings.Tournament::getNameUsedInGame, Collectors.counting()))
                            .entrySet().stream()
                            .max(Map.Entry.comparingByValue())
                            .map(Map.Entry::getKey)
                            .orElse(playerRanking.getCommonPlayerName());
                    playerRanking.setCommonPlayerName(mostUsedName);
                });
                TournamentRankings.SeasonRanking seasonRanking = seasonRankingsMap.get(season.seasonId);
                seasonRanking.setRankings(sortedPlayerRankingsForSeason);
                seasons.add(seasonRanking);
            });

            return tournamentRankings;
        }
    }

    private String getSeasonId(Tournament tournament) {
        Season[] values = Season.values();
        for (Season value : values) {
            if(tournament.getCreatedAt()
                    .isBefore(value.getSeasonEndDate())){
                return value.getSeasonId();
            }
        }
        return null;
    }

    private int getPointByIRLSystem(Integer position){
        if(position == 1){
            return 25;
        }
        if(position == 2){
            return 20;
        }
        if(position == 3){
            return 16;
        }
        if(position == 4){
            return 13;
        }
        if(position == 5){
            return 11;
        }
        if(position == 6){
            return 9;
        }
        if(position == 7){
            return 7;
        }
        if(position == 8){
            return 5;
        }
        if(position == 9){
            return 3;
        }
        if(position == 10){
            return 2;
        }
        if(position == 11){
            return 1;
        }
        return 0;
    }
}

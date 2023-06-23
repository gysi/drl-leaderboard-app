package de.gregord.drlleaderboardbackend.services;

import de.gregord.drlleaderboardbackend.domain.Season;
import de.gregord.drlleaderboardbackend.domain.TournamentRankings;
import de.gregord.drlleaderboardbackend.entities.Tournament;
import de.gregord.drlleaderboardbackend.entities.tournament.TournamentRanking;
import de.gregord.drlleaderboardbackend.repositories.TournamentRepository;
import org.slf4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.gregord.drlleaderboardbackend.domain.Season.SEASON_MAPPING_BY_SEASON_ID;

@Service
public class TournamentService {
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(TournamentService.class);

    TournamentRepository tournamentRepository;

    public TournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @Transactional(readOnly = true)
    @Cacheable("tournamentRankings")
    public TournamentRankings getTournamentRankingForSeason(String seasonId) {
        Season season = SEASON_MAPPING_BY_SEASON_ID.get(seasonId);
        if(season == null){
            throw new IllegalArgumentException("No season found for seasonId: " + seasonId);
        }
        try(Stream<Tournament> tournaments = tournamentRepository.streamTournaments(season.getSeasonStartDate(), season.getSeasonEndDate())) {
            // Map<PlayerId, PlayerRanking>
            Map<String, TournamentRankings.PlayerRanking> playerRankingMap = new java.util.HashMap<>();

            tournaments.forEach(tournament -> {
                List<TournamentRanking> rankings = tournament.getRankings();
                for (int i = 0; i < rankings.size(); i++) {
                    int position = i+1;
//                    if(position > 11){ // 12th place and above get no points
//                        break;
//                    }
                    TournamentRanking ranking = rankings.get(i);

                    // Player ranking
                    TournamentRankings.PlayerRanking playerRanking = playerRankingMap.computeIfAbsent(ranking.getPlayerId(), playerId -> {
                        TournamentRankings.PlayerRanking newPlayerRanking = new TournamentRankings.PlayerRanking();
                        newPlayerRanking.setCommonPlayerName(ranking.getProfileName());
                        return newPlayerRanking;
                    });
                    playerRanking.setCommonPlayerName(ranking.getProfileName());
                    playerRanking.incrementNumberOfTournamentsPlayed();
                    if (ranking.getGoldenPos() != null && ranking.getGoldenPos() != 0) {
                        playerRanking.incrementNumberOfGoldenHeats();
                    }
                    playerRanking.setTotalPoints(playerRanking.getTotalPoints() + getPointByIRLSystem(position));

                    // Tournament
                    TournamentRankings.Tournament tournamentPlayerParticipatedIn = new TournamentRankings.Tournament();
                    tournamentPlayerParticipatedIn.setNameUsedInGame(ranking.getProfileName());
                    tournamentPlayerParticipatedIn.setTitle(tournament.getTitle());
                    tournamentPlayerParticipatedIn.setPosition(position);
                    tournamentPlayerParticipatedIn.setPoints(getPointByIRLSystem(position));
                    tournamentPlayerParticipatedIn.setStartDate(tournament.getRegistrationEndAt());

                    playerRanking.getPlayedTournaments().addFirst(tournamentPlayerParticipatedIn);
                }
            });

            TournamentRankings tournamentRankings = new TournamentRankings();
            tournamentRankings.setSeasonName(season.getSeasonName());
            tournamentRankings.setSeasonStartDate(season.getSeasonStartDate());
            tournamentRankings.setSeasonEndDate(season.getSeasonEndDate());

            playerRankingMap.values().forEach(playerRanking -> {
                // get and set best 12 positions in the player ranking
                List<Integer> best12Positions = playerRanking.getPlayedTournaments().stream()
                        .map(TournamentRankings.Tournament::getPosition)
                        .sorted()
                        .limit(12).toList();
                playerRanking.setBest12Positions(best12Positions);
                // set points for each seasion based on the tournamentWeeks
                int points = playerRanking.getPlayedTournaments().stream()
                        .mapToInt(TournamentRankings.Tournament::getPoints)
                        .boxed()
                        .sorted(Collections.reverseOrder())
                        .limit(12)
                        .mapToInt(Integer::intValue)
                        .sum();
                playerRanking.setPointsBest12Tournaments(points);
            });

            // Sort PlayerRankings by points
            List<TournamentRankings.PlayerRanking> sortedPlayerRankings = playerRankingMap.values().stream()
                    .sorted((o1, o2) -> o2.getPointsBest12Tournaments().compareTo(o1.getPointsBest12Tournaments()))
                    .collect(Collectors.toList());

            for (int i = 0; i < sortedPlayerRankings.size(); i++) {
                sortedPlayerRankings.get(i).setPosition(i+1);
            }

            // Set most used name as common name for each PlayerRanking
            sortedPlayerRankings.forEach(playerRanking -> {
                String mostUsedName = playerRanking.getPlayedTournaments().stream()
                        .collect(Collectors.groupingBy(TournamentRankings.Tournament::getNameUsedInGame, Collectors.counting()))
                        .entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse(playerRanking.getCommonPlayerName());
                playerRanking.setCommonPlayerName(mostUsedName);
            });

            tournamentRankings.setRankings(sortedPlayerRankings);

            return tournamentRankings;
        }
    }

    private String getSeasonId(Tournament tournament) {
        Season[] values = Season.values();
        for (Season value : values) {
            if(tournament.getRegistrationEndAt()
                    .isBefore(value.getSeasonEndDate())){
                return value.getSeasonId();
            }
        }
        return null;
    }

//    public static void main(String[] args) {
//        int[] ints = {1, 2, 2, 3, 3, 3, 3, 5, 5, 6, 6, 7};
//        // getPointByIRLSystem for each number and sum them up
//        int sum = Arrays.stream(ints)
//                .map(TournamentService::getPointByIRLSystem)
//                .sum();
//        System.out.println(sum);
//    }

    private static int getPointByIRLSystem(Integer position){
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

package de.gregord.drlleaderboardbackend.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;

@Data
public class TournamentRankings {

    @Data
    public static class PlayerRanking {
        String commonPlayerName;
        String profileThumb;
        String flagUrl;
        String platform;
        Integer numberOfTournamentsPlayed = 0;
        Integer numberOfGoldenHeats = 0;
        Integer totalPoints = 0;
        Integer position;
        Integer pointsBest12Tournaments = 0;
        List<Integer> best12Positions = new ArrayList<>();
        List<Integer> allPositions = new ArrayList<>();
        LinkedList<Tournament> playedTournaments = new LinkedList<>();

        public void incrementNumberOfTournamentsPlayed() {
            this.numberOfTournamentsPlayed++;
        }

        public void incrementNumberOfGoldenHeats() {
            this.numberOfGoldenHeats++;
        }
    }

    @Data
    public static class Tournament {
        String title;
        LocalDateTime startDate;
        Integer position;
        Integer points;
        String nameUsedInGame;
    }

    private String seasonName;
    private LocalDateTime seasonStartDate;
    private LocalDateTime seasonEndDate;
    private List<PlayerRanking> rankings = new ArrayList<>();
}

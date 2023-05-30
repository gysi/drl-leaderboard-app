package de.gregord.drlleaderboardbackend.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
public class TournamentRankings {

    @Data
    public static class SeasonRanking {
        private String seasonName;
        private List<PlayerRanking> rankings = new ArrayList<>();

        public SeasonRanking(String seasonName) {
            this.seasonName = seasonName;
        }
    }

    @Data
    public static class PlayerRanking {
        String commonPlayerName;
        Integer numberOfTournamentsPlayed = 0;
        Integer points = 0;
        LinkedList<Tournament> tournaments = new LinkedList<>();

        public void addPoints(Integer points) {
            this.points += points;
        }

        public void incrementNumberOfTournamentsPlayed() {
            this.numberOfTournamentsPlayed++;
        }
    }

    @Data
    public static class Tournament {
        String title;
        LocalDateTime startDate;
        Integer position;
        String nameUsedInGame;
    }

    private List<SeasonRanking> seasons = new ArrayList<>();

}

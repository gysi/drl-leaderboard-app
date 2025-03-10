package de.gregord.drlleaderboardbackend.domain;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class TournamentSeason {
    public enum TournamentStatus { COMPLETED, CANCELLED, UPCOMING }

    @Data
    public static class Tournament {
        LocalDateTime startTime;
        TournamentStatus status;

        public Tournament(LocalDateTime startTime, TournamentStatus status) {
            this.startTime = startTime;
            this.status = status;
        }
    }
    List<Tournament> tournaments;
    LocalDateTime startDate;
    LocalDateTime endDate;

    public TournamentSeason(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.tournaments = generateTournaments(startDate, endDate);
    }

    private List<Tournament> generateTournaments(LocalDateTime start, LocalDateTime end) {
        List<Tournament> tournaments = new ArrayList<>();

        for (LocalDateTime date = start; date.isBefore(end); date = date.plusDays(1)) {
            switch (date.getDayOfWeek()) {
                case TUESDAY:
                case THURSDAY:
                case SUNDAY:
                    tournaments.add(new Tournament(date.withHour(18).withMinute(30), TournamentStatus.UPCOMING));
                    break;
                case MONDAY:
                case WEDNESDAY:
                case FRIDAY:
                    tournaments.add(new Tournament(date.withHour(1).withMinute(0), TournamentStatus.UPCOMING));
                    break;
            }
        }
        return tournaments;
    }

    public void checkStatusOfTournaments(List<Tournament> pastTournaments) {
        for (Tournament pastTournament : pastTournaments) {
            for (Tournament upcomingTournament : tournaments) {
                Duration timeDifference = Duration.between(pastTournament.startTime, upcomingTournament.startTime).abs();
                // Check if the tournaments are within 2 hours of each other
                if (timeDifference.toHours() <= 2) {
                    upcomingTournament.status = pastTournament.status;
                } else if (upcomingTournament.startTime.isBefore(pastTournament.startTime) &&
                        upcomingTournament.status == TournamentStatus.UPCOMING) {
                    upcomingTournament.status = TournamentStatus.CANCELLED;
                }
            }
        }
    }
}

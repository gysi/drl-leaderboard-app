package de.gregord.drlleaderboardbackend.entities.tournament;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TournamentRound implements Serializable {

    @Getter
    @Setter
    public static class Match implements Serializable {
        private String id; // drl api mapping: id
        private List<Player> players; // drl api mapping: players
        private List<String> playerOrder; // drl api mapping: player-order, empty list if not started
        private String status; // drl api mapping: status, values: 'idle', 'complete' TODO: status of running tournament?
        private Integer nOrder; // drl api mapping: norder
        private Integer numWinners; // drl api mapping: num-winners, number of players that will advance to the next round
        private Integer heats; // drl api mapping: heats
        private Integer activeHeat; // drl api mapping: active-heat
        private Integer currentHeat; // drl api mapping: current-heat
        private Integer playersSize; // drl api mapping: players-size, same as Tournament playersSize if Round mode is leaderboard
    }

    @Getter
    @Setter
    public static class Player implements Serializable {
        private String playerId; // drl api mapping: player-id
        private String steamId; // drl api mapping: steam-id
        private String profileName; // drl api mapping: profile-name
        private String profileColor; // drl api mapping: profile-color
        private String profileThumb; // drl api mapping: profile-thumb
        private String flagUrl; // drl api mapping: flag-url
        private String platform; // drl api mapping: platform
    }

    private String id; // drl api mapping: id
    private Integer nOrder; // drl api mapping: norder
    private String title; // drl api mapping: title
    private String status; // drl api mapping: status, values: 'idle', 'complete'
    private String mode; // drl api mapping: mode, values: 'leaderboard', 'sudden_death'
    private LocalDateTime startAt; // drl api mapping: start-at
    private LocalDateTime endAt; // drl api mapping: end-at

    private List<Match> matches; // drl api mapping: matches
}

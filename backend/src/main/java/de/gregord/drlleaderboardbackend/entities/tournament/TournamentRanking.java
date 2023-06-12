package de.gregord.drlleaderboardbackend.entities.tournament;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TournamentRanking implements Serializable {
    private String playerId; // drl api mapping: player-id
    private String steamId; // drl api mapping: steam-id
    private String profileName; // drl api mapping: profile-name
    private String profileColor; // drl api mapping: profile-color
    private String profileThumb; // drl api mapping: profile-thumb
    private String flagUrl; // drl api mapping: flag-url
    private String platform; // drl api mapping: platform
    //    private Integer position; // drl api mapping: position
    private Long score; // drl api mapping: score
    private Long scoreTotal; // drl api mapping: score_total
    private Long orderScoreTotal; // drl api mapping: order_score_total
    private Integer goldenPos; // drl api mapping: golden_pos
}

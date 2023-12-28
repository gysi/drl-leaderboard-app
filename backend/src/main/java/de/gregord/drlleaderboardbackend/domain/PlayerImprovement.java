package de.gregord.drlleaderboardbackend.domain;

import de.gregord.drlleaderboardbackend.entities.TrackMinimal;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PlayerImprovement {
    private String playerName;
    private Long previousPosition;
    private Long currentPosition;
    private LocalDateTime createdAt;
    private TrackMinimal track;
    private String profilePicture;
    private Boolean forcePost = false;
}

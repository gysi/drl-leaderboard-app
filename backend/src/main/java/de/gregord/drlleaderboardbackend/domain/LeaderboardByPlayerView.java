package de.gregord.drlleaderboardbackend.domain;

import java.time.LocalDateTime;
import java.util.List;

public interface LeaderboardByPlayerView {
    Long getId();
    String getPlayerName();
    Long getPosition();
    Long getScore();
    Double getPoints();
    LocalDateTime getCreatedAt();
    Integer getCrashCount();
    Double getTopSpeed();
    String getDroneName();
    Boolean getIsInvalidRun();
    String getInvalidRunReason();
    Track getTrack();
    List<LeaderboardEntryMinimal> getBeatenBy();

    interface Track {
        Long getId();
        String getName();
        String getMapName();
        String getParentCategory();
    }

    interface LeaderboardEntryMinimal {
        Long getId();
        String getPlayerName();
        Long getPosition();
        Long getScore();
        Double getPoints();
        LocalDateTime getCreatedAt();
    }
}

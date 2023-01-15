package de.gregord.drlleaderboardbackend.domain;

import java.time.LocalDateTime;
import java.util.List;

public interface LeaderboardByPlayerView {
    String getId();
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
    LeaderboardByPlayerView_Track getTrack();
    List<LeaderboardByPlayerView_LeaderboardEntryMinimal> getBeatenBy();

    interface LeaderboardByPlayerView_Track {
        String getId();
        String getName();
        String getMapName();
        String getParentCategory();
    }

    interface LeaderboardByPlayerView_LeaderboardEntryMinimal {
        String getId();
        String getPlayerName();
        Long getPosition();
        Long getScore();
        Double getPoints();
        LocalDateTime getCreatedAt();
    }
}

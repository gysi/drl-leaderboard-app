package de.gregord.drlleaderboardbackend.domain;

import java.time.LocalDateTime;

public interface LeaderboardByTrackView {
    String getId();
    LeaderboardByTrackView_Player getPlayer();
    Long getPosition();
    Long getScore();
    Double getPoints();
    Integer getCrashCount();
    Double getTopSpeed();
    String getDroneName();
    Boolean getisInvalidRun();
    String getInvalidRunReason();
    LocalDateTime getCreatedAt();

    interface LeaderboardByTrackView_Player {
        String getPlayerName();
        String getProfileThumb();
        String getProfilePlatform();
        String getFlagUrl();
    }
}

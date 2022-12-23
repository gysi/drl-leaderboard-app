package de.gregord.drlleaderboardbackend.domain;

import java.time.LocalDateTime;

public interface LeaderBoardByTrackView {
    Long getId();
    String getPlayerName();
    Long getPosition();
    Long getScore();
    Double getPoints();
    Integer getCrashCount();
    Double getTopSpeed();
    String getProfileThumb();
    String getProfilePlatform();
    String getFlagUrl();
    String getDroneName();
    Boolean isInvalidRun();
    String getInvalidRunReason();
    LocalDateTime getCreatedAt();
}

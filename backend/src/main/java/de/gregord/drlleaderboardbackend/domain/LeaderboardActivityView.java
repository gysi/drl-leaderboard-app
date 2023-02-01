package de.gregord.drlleaderboardbackend.domain;

import java.time.LocalDateTime;

public interface LeaderboardActivityView {
    String getPlayerName();
    Long getPosition();
    LocalDateTime getCreatedAt();
    String getTrackId();
    String getTrackName();
    String getMapName();
    String getParentCategory();
}

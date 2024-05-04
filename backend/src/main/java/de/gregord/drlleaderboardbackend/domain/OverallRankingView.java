package de.gregord.drlleaderboardbackend.domain;

import java.time.LocalDateTime;

public interface OverallRankingView {
    Long getPosition();
    String getPlayerName();
    Long getTotalPoints();
    Double getAvgPosition();
    Long getBestPosition();
    Long getInvalidRuns();
    Long getCompletedTracks();
    Integer getTotalCrashCount();
    Long getTotalScore();
    Double getMaxTopSpeed();
    String getFlagUrl();
    String getProfilePlatform();
    String getProfileThumb();
    LocalDateTime getLatestActivity();
}

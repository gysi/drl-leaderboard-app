package de.gregord.drlleaderboardbackend.domain;

import java.time.LocalDateTime;

public interface CommunityRankingView {
    Long getPosition();
    Long getPlayerId();
    String getPlayerName();
    Double getTotalPoints();
    Double getAvgPosition();
    Long getBestPosition();
    Long getInvalidRuns();
    Long getCompletedTracks();
    Long getTotalCrashCount();
    Long getTotalScore();
    Double getMaxTopSpeed();
    String getFlagUrl();
    String getProfilePlatform();
    String getProfileThumb();
    LocalDateTime getLatestActivity();
    Boolean getIsEligible();
}

package de.gregord.drlleaderboardbackend.domain;

import java.time.LocalDateTime;

public interface CommunityRankingView {
    Long getPosition();

    String getPlayerName();

    Long getTotalPoints();

    Double getAvgPosition();

    Long getInvalidRuns();

    Long getCompletedTracks();

    Integer getTotalCrashCount();

    Long getTotalScore();

    Double getMaxTopSpeed();

    String getFlagUrl();

    String getProfilePlatform();

    String getProfileThumb();

    LocalDateTime getLatestActivity();

    Boolean getIsEligible();
}
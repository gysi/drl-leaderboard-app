package de.gregord.drlleaderboardbackend.domain;

public interface OverallRankingView {
    Long getPosition();
    String getPlayerName();
    Long getTotalPoints();
    Long getInvalidRuns();
    Long getCompletedTracks();
    Integer getTotalCrashCount();
    Long getTotalScore();
    Double getMaxTopSpeed();
}

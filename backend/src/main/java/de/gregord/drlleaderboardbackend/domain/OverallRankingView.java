package de.gregord.drlleaderboardbackend.domain;

public interface OverallRankingView {
    String getPlayerName();
    Long getTotalPoints();
    Long getInvalidRuns();
    Long getCompletedTracks();
    Integer getTotalCrashCount();
    Long getTotalScore();
    Double getMaxTopSpeed();
}

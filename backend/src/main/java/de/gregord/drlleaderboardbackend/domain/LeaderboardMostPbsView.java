package de.gregord.drlleaderboardbackend.domain;

public interface LeaderboardMostPbsView {
    String getPlayerName();
    Long getEntries();
    Long getBestPosition();
    Double getAvgPosition();
}

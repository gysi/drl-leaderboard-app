package de.gregord.drlleaderboardbackend.domain;

public interface CommunityRankingHistoryView {
    Long getPosition();
    String getPlayerId();
    String getPlayerName();
    Double getTotalPoints();
    Double getAvgPosition();
    Long getBestPosition();
    Long getInvalidRuns();
    Long getCompletedTracks();
    Long getTotalCrashCount();
    Long getTotalPenalties();
    Long getTotalScore();
    Double getMaxTopSpeed();
    String getFlagUrl();
    String getProfilePlatform();
    String getProfileThumb();
}

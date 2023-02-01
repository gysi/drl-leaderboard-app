package de.gregord.drlleaderboardbackend.domain;

public interface LeaderboardMostTrackEntriesView {
    String getId();
    String getName();
    String getMapName();
    String getParentCategory();
    Long getEntries();
}

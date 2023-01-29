package de.gregord.drlleaderboardbackend.domain;

import java.util.List;

public interface WorstTracksView {
    String getTrackId();
    String getTrackName();
    String getTrackMapName();
    String getTrackParentCategory();
    List<String> getReasons();
}

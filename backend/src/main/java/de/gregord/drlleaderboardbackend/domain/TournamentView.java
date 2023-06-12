package de.gregord.drlleaderboardbackend.domain;

import java.time.LocalDateTime;
import java.util.List;

public interface TournamentView {
    String getGuid();
    String getTitle();
    String getTrackName();
    List<String> getTop3();
    LocalDateTime getStartDate();
}

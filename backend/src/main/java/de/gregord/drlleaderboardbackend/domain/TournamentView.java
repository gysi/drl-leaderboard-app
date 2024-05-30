package de.gregord.drlleaderboardbackend.domain;

import com.fasterxml.jackson.annotation.JsonRawValue;

import java.time.LocalDateTime;

public interface TournamentView {
    String getGuid();
    String getTitle();
//    String getTrackName(); // Don't show the trackname within the api.
    String getImgUrl();
    @JsonRawValue
    String getTop10();
    LocalDateTime getStartDate();
    String getStatus();
}

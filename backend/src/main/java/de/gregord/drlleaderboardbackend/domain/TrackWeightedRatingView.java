package de.gregord.drlleaderboardbackend.domain;

public interface TrackWeightedRatingView {
    Long getId();
    String getGuid();
    String getMapId();
    String getDrlTrackId();
    String getName();
    String getTrackCreator();
    String getMapDifficulty();
    Long getRatingCount();
    Double getRatingScore();
    Double getWeightedRating();
    Double getTournamentBoost();
}

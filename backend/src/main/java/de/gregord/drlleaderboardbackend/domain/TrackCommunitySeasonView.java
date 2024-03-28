package de.gregord.drlleaderboardbackend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;

public interface TrackCommunitySeasonView {
    @JsonProperty("id")
    @Value("#{target.track.id}")
    String getTrackId();

    @JsonProperty("name")
    @Value("#{target.track.name}")
    String getTrackName();

    @JsonProperty("mapName")
    @Value("#{target.track.mapName}")
    String getTrackMapName();

    @JsonProperty("parentCategory")
    @Value("#{target.track.parentCategory}")
    String getTrackParentCategory();

    @JsonProperty("mapThumb")
    @Value("#{target.track.mapThumb}")
    String getTrackMapThumb();

    @JsonProperty("trackCreator")
    @Value("#{target.track.trackCreator}")
    String getTrackTrackCreator();

    @JsonProperty("ratingScore")
    @Value("#{target.track.ratingScore}")
    Double getTrackRatingScore();

    @JsonProperty("ratingCount")
    @Value("#{target.track.ratingCount}")
    Integer getTrackRatingCount();

    @JsonProperty("trackDifficulty")
    @Value("#{target.track.mapDifficulty}")
    Integer getTrackMapDifficulty();

    Integer getCustomTrackDifficulty();
}

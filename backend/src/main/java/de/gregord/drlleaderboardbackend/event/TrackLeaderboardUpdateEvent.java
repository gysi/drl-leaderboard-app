package de.gregord.drlleaderboardbackend.event;

import de.gregord.drlleaderboardbackend.entities.LeaderboardEntry;
import de.gregord.drlleaderboardbackend.entities.Track;
import org.springframework.context.ApplicationEvent;

import java.util.ArrayList;
import java.util.List;

public class TrackLeaderboardUpdateEvent extends ApplicationEvent {
    private final Track track;
    private List<LeaderboardEntry> updatedLeaderboardEntries = new ArrayList<>();

    public TrackLeaderboardUpdateEvent(Object source, Track track, List<LeaderboardEntry> updatedLeaderboardEntries) {
        super(source);
        this.track = track;
        this.updatedLeaderboardEntries = updatedLeaderboardEntries;
    }

    public Track getTrack() {
        return track;
    }

    public List<LeaderboardEntry> getUpdatedLeaderboardEntries() {
        return updatedLeaderboardEntries;
    }
}

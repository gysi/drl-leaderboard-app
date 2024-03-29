package de.gregord.drlleaderboardbackend.services;

import de.gregord.drlleaderboardbackend.entities.LeaderboardEntry;

import java.util.Map;

public interface LeaderboardProcessor {
    /**
     * This allows you to change the newOrUpdatedLeaderboardEntry or apply some additional logic.
     * @param isNewTrack true if there are no existing entries within the db.
     * @param drlLeaderboardEntry Map from the json response of the drl leaderboard entry
     * @param existingEntry Existing LeaderboardEntry from DB or null
     * @param newOrUpdatedLeaderboardEntry current leaderboardEntry that is being processed
     * @param currentLeaderboardEntriesByPlayerId key of this Map is the DRL player id
     * @param leaderScore drl api score for the leading player of this track which is the time in milliseconds
     */
    void process(boolean isNewTrack,
                 Map<String, Object> drlLeaderboardEntry,
                 LeaderboardEntry existingEntry,
                 LeaderboardEntry newOrUpdatedLeaderboardEntry,
                 Map<String, LeaderboardEntry> currentLeaderboardEntriesByPlayerId,
                 Long leaderScore);
}

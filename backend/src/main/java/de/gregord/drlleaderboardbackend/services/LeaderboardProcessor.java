package de.gregord.drlleaderboardbackend.services;

import de.gregord.drlleaderboardbackend.entities.LeaderboardEntry;

import java.util.Map;

public interface LeaderboardProcessor {
    /**
     * This allows you to change the newOrUpdatedLeaderboardEntry or apply some additional logic.
     * @param drlLeaderboardEntry Map from the json response of the drl leaderboard entry
     * @param newOrUpdatedLeaderboardEntry current leaderboardEntry that is being processed
     * @param currentLeaderboardEntriesByPlayerId key of this Map is the DRL player id
     * @param leaderScore drl api score for the leading player of this track which is the time in milliseconds
     * @param isExistingEntryInvalid if there was an existing entry for for this player already, this var tells if it was invalid or not
     */
    void process(Map<String, Object> drlLeaderboardEntry, LeaderboardEntry newOrUpdatedLeaderboardEntry,
                 Map<String, LeaderboardEntry> currentLeaderboardEntriesByPlayerId,
                 Long leaderScore, Boolean isExistingEntryInvalid);
}

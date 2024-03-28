package de.gregord.drlleaderboardbackend.services;

import de.gregord.drlleaderboardbackend.entities.LeaderboardEntry;
import de.gregord.drlleaderboardbackend.entities.Player;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class LeaderboardProcessorResult {
    private long responseContentLength;
    private List<Player> newPlayers = new ArrayList<>();
    private List<Player> updatedPlayers = new ArrayList<>();
    private Collection<LeaderboardEntry> newOrUpdatedLeaderboardEntries = new ArrayList<>();
    private Collection<LeaderboardEntry> unchangedLeaderboardEntries = new ArrayList<>();
    private Collection<LeaderboardEntry> existingLeaderboardEntriesThatWerentProcessedAgain = new ArrayList<>();
}

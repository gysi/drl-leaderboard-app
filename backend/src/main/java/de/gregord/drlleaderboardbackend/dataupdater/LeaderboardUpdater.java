package de.gregord.drlleaderboardbackend.dataupdater;

import de.gregord.drlleaderboardbackend.domain.PlayerImprovement;
import de.gregord.drlleaderboardbackend.entities.LeaderboardEntry;
import de.gregord.drlleaderboardbackend.entities.Track;
import de.gregord.drlleaderboardbackend.services.DRLApiService;
import de.gregord.drlleaderboardbackend.services.LeaderboardProcessorResult;
import de.gregord.drlleaderboardbackend.services.LeaderboardService;
import de.gregord.drlleaderboardbackend.services.PlayerService;
import de.gregord.drlleaderboardbackend.services.discord.DiscordMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class LeaderboardUpdater {
    public static Logger LOG = LoggerFactory.getLogger(LeaderboardUpdater.class);

    private static final int PAGE_LIMIT = 50;
    private static final int MAX_ENTRIES_PER_TRACK = 200;
    private static final Long totalContentLength = 0L;
    private static Long totalRequestCount = 0L;

    private final LeaderboardService leaderboardService;
    private final DiscordMessageService discordService;
    private final PlayerService playerService;
    private final DRLApiService drlApiService;

    public LeaderboardUpdater(
            LeaderboardService leaderboardService,
            DiscordMessageService discordService,
            PlayerService playerService,
            DRLApiService drlApiService
    ) {
        this.leaderboardService = leaderboardService;
        this.discordService = discordService;
        this.playerService = playerService;
        this.drlApiService = drlApiService;
    }

    public void updateLeaderboardForTrack(Track track) {
        try {
            LOG.info("update leaderboard for track: {} ...", track.getName());
            List<PlayerImprovement> improvements = new ArrayList<>();
            LeaderboardProcessorResult leaderboardProcessorResult = drlApiService.getAndProcessLeaderboardEntries(
                    track, PAGE_LIMIT, MAX_ENTRIES_PER_TRACK,
                    (isNewTrack,
                     drlLeaderboardEntry,
                     existingEntry,
                     newOrUpdatedLeaderboardEntry,
                     currentLeaderboardEntriesForTrack,
                     leaderScore) -> {
                        totalRequestCount++;
                        if (!isNewTrack
                                && newOrUpdatedLeaderboardEntry.getPosition() <= 50
                                && (existingEntry == null
                                    || newOrUpdatedLeaderboardEntry.getPosition() < existingEntry.getPosition()
                                    /* When the DRL Api doesn't give me a replay URL I flag the run as invalid. But it happens often
                                       that the replay is there later on because the DRL System is slow to upload it and save it
                                       within the player pb. Until thats happened my site reports that PB as invalid.
                                       Now my DRL Bot goes through all new pbs that have a new position for all tracks every ~10Minutes.
                                       If there are any then it sends them to discord (But not invalid ones). It then saves the creation
                                       time from the latest PB it posted and saves it. This way I know what I already posted so that I
                                       don't post twice..
                                       Now if your run then later gets a replay attached to it, I will update your PB, but I won't post it
                                       because its creation time is before the time my bot last posted.
                                       This next condition fixes this: */
                                    || (Boolean.TRUE.equals(existingEntry.getIsInvalidRun())
                                        && Boolean.FALSE.equals(newOrUpdatedLeaderboardEntry.getIsInvalidRun())))
                                && Boolean.FALSE.equals(newOrUpdatedLeaderboardEntry.getIsInvalidRun())) {
                            PlayerImprovement improvement = new PlayerImprovement();
                            improvement.setPlayerName(newOrUpdatedLeaderboardEntry.getPlayer().getPlayerName());
                            improvement.setPreviousPosition(newOrUpdatedLeaderboardEntry.getPreviousPosition());
                            improvement.setCurrentPosition(newOrUpdatedLeaderboardEntry.getPosition());
                            improvement.setPreviousScore(newOrUpdatedLeaderboardEntry.getPreviousScore());
                            improvement.setCurrentScore(newOrUpdatedLeaderboardEntry.getScore());
                            improvement.setCreatedAt(newOrUpdatedLeaderboardEntry.getCreatedAt());
                            improvement.setTrack(newOrUpdatedLeaderboardEntry.getTrack());
                            improvement.setProfilePicture(newOrUpdatedLeaderboardEntry.getPlayer().getProfileThumb());
                            improvement.setLeaderScore(leaderScore);
                            if (existingEntry != null && Boolean.TRUE.equals(existingEntry.getIsInvalidRun())) {
                                improvement.setForcePost(true);
                            }
                            improvements.add(improvement);
                        }
                    }
            );
            LOG.info("Saving leaderboard entries for track " + track.getName() + " with map id " + track.getMapId() + " and map name " + track.getMapName());
            playerService.savePlayers(leaderboardProcessorResult.getNewPlayers(), leaderboardProcessorResult.getUpdatedPlayers());
            leaderboardService.saveAndDeleteLeaderboardEntriesForTrack(
                    track,
                    leaderboardProcessorResult.getNewOrUpdatedLeaderboardEntries(),
                    leaderboardProcessorResult.getUnchangedLeaderboardEntries(),
                    leaderboardProcessorResult.getExistingLeaderboardEntriesThatWerentProcessedAgain()
            );
            discordService.sendMessageToLeaderboardPostsChannels(improvements);
        } catch (Exception e) {
            LOG.error(String.format("Error while updating leaderboard entries for track %s with map id %s and map name %s",
                    track.getName(), track.getMapId(), track.getMapName()), e);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                LOG.error("Interrupted while sleeping after error", e);
            }
        }
    }
}

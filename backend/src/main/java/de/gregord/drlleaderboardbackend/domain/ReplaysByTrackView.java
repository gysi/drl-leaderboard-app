package de.gregord.drlleaderboardbackend.domain;

public interface ReplaysByTrackView {
    String getPosition();
    ReplaysByTrackView_Player getPlayer();
    String getReplayUrl();

    interface ReplaysByTrackView_Player {
        String getPlayerName();
    }
}

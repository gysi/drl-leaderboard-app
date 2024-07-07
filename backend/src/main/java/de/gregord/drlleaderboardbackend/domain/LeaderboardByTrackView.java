package de.gregord.drlleaderboardbackend.domain;

import com.fasterxml.jackson.annotation.JsonRawValue;
import de.gregord.drlleaderboardbackend.entities.LeaderboardEntry;

import java.time.LocalDateTime;
import java.util.List;

public interface LeaderboardByTrackView {
    String getId();
    LeaderboardByTrackView_Player getPlayer();
    Long getPosition();
    Long getScore();
    Long getTimePenaltyTotal();
    List<LeaderboardByTrackView_Penalty> getPenalties();
    Double getPoints();
    Integer getCrashCount();
    Double getTopSpeed();
    String getDroneName();
    Boolean getisInvalidRun();
    String getInvalidRunReason();
    LocalDateTime getCreatedAt();

    interface LeaderboardByTrackView_Player {
        String getPlayerName();
        String getProfileThumb();
        String getProfilePlatform();
        String getFlagUrl();
    }

    interface LeaderboardByTrackView_Penalty {
        String getType();
        Object getTypeData();
        Integer getTimePenalty();
        Long getTimePosition();
    }
}

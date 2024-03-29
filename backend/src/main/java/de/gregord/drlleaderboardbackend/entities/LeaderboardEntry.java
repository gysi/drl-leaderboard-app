package de.gregord.drlleaderboardbackend.entities;

import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "leaderboards", indexes = {
        @Index(columnList = "track_id, player_id", unique = true),
        @Index(columnList = "track_id"),
        @Index(columnList = "player_id"),
        @Index(columnList = "playerIdDrl"),
        @Index(columnList = "score"),
        @Index(columnList = "position"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "updatedAt"),
        @Index(columnList = "points"),
        @Index(columnList = "isInvalidRun")
})
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class LeaderboardEntry {
    @Id
    @GeneratedValue(generator = TsidGenerator.GENERATOR_NAME)
    @GenericGenerator(name = TsidGenerator.GENERATOR_NAME, type = TsidGenerator.class)
    @EqualsAndHashCode.Include
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", nullable = false)
    private TrackMinimal track;
    private String drlId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id", nullable = false)
    @Fetch(FetchMode.JOIN)
    private Player player;
    private String playerIdDrl;
    private Long score;
    private Long previousScore;
    private Integer crashCount;
    private Double topSpeed;
    private Long position;
    private Long previousPosition;
    private Double points;
    // from drl api
    private LocalDateTime createdAt;
    private String droneName;
    private String replayUrl;
    private Boolean isInvalidRun = false;
    private String invalidRunReason = null;
    private LocalDateTime updatedAt;
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "leaderboards_beaten_by",
            joinColumns = {@JoinColumn(name = "leaderboard_id")},
            inverseJoinColumns = {@JoinColumn(name = "beaten_by_leaderboard_id", referencedColumnName = "id")})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<LeaderboardEntryMinimal> beatenBy = new ArrayList<>();

    public static boolean equalsForUpdate(LeaderboardEntry thiz, LeaderboardEntry that) {
        if (thiz == that) return true;

        if (thiz == null || that == null) return false;

        return new EqualsBuilder()
                .append(thiz.id, that.id)
                .append(thiz.drlId, that.drlId)
                .append(thiz.playerIdDrl, that.playerIdDrl)
                .append(thiz.score, that.score)
                .append(thiz.previousScore, that.previousScore)
                .append(thiz.crashCount, that.crashCount)
                .append(thiz.topSpeed, that.topSpeed)
                .append(thiz.position, that.position)
                .append(thiz.previousPosition, that.previousPosition)
                .append(thiz.points, that.points)
                .append(thiz.createdAt, that.createdAt)
                .append(thiz.droneName, that.droneName)
                .append(thiz.replayUrl, that.replayUrl)
                .append(thiz.isInvalidRun, that.isInvalidRun)
                .append(thiz.invalidRunReason, that.invalidRunReason)
                .append(thiz.updatedAt, that.updatedAt).isEquals();
    }

    public static LeaderboardEntry simpleCopy(LeaderboardEntry toCopy) {
        LeaderboardEntry leaderboardEntry = new LeaderboardEntry();
        leaderboardEntry.id = toCopy.id;
        leaderboardEntry.drlId = toCopy.drlId;
        leaderboardEntry.playerIdDrl = toCopy.playerIdDrl;
        leaderboardEntry.score = toCopy.score;
        leaderboardEntry.previousScore = toCopy.previousScore;
        leaderboardEntry.crashCount = toCopy.crashCount;
        leaderboardEntry.topSpeed = toCopy.topSpeed;
        leaderboardEntry.position = toCopy.position;
        leaderboardEntry.previousPosition = toCopy.previousPosition;
        leaderboardEntry.points = toCopy.points;
        leaderboardEntry.createdAt = toCopy.createdAt;
        leaderboardEntry.droneName = toCopy.droneName;
        leaderboardEntry.replayUrl = toCopy.replayUrl;
        leaderboardEntry.isInvalidRun = toCopy.isInvalidRun;
        leaderboardEntry.invalidRunReason = toCopy.invalidRunReason;
        leaderboardEntry.updatedAt = toCopy.updatedAt;
        return leaderboardEntry;
    }
}

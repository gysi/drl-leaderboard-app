package de.gregord.drlleaderboardbackend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "leaderboards", indexes = {
        @Index(columnList = "track_id, playerId", unique = true),
        @Index(columnList = "track_id"),
        @Index(columnList = "playerId"),
        @Index(columnList = "playerName"),
        @Index(columnList = "score"),
        @Index(columnList = "position"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "updatedAt"),
        @Index(columnList = "points"),
        @Index(columnList = "isInvalidRun")
})
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class LeaderboardEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", nullable = false)
    private TrackMinimal track;
    private String playerId;
    private String playerName;
    private Long score;
    private Integer crashCount;
    private Double topSpeed;
    private Long position;
    private Double points;
    // from drl api
    private LocalDateTime createdAt;
    private String profileThumb;
    private String profilePlatform;
    private String profilePlatformId;
    private String flagUrl;
    private String droneName;
    private String replayUrl;
    private Boolean isInvalidRun = false;
    private String invalidRunReason = null;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "leaderboards_beaten_by",
            joinColumns = {@JoinColumn(name = "leaderboard_id")},
            inverseJoinColumns = {@JoinColumn(name = "beaten_by_leaderboard_id", referencedColumnName = "id")})
    private List<LeaderboardEntryMinimal> beatenBy = new ArrayList<>();
}

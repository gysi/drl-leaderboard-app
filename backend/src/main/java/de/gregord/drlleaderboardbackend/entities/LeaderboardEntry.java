package de.gregord.drlleaderboardbackend.entities;

import jakarta.persistence.*;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;
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
    @GenericGenerator(name = TsidGenerator.GENERATOR_NAME, strategy = TsidGenerator.STRATEGY_NAME)
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
    private Integer crashCount;
    private Double topSpeed;
    private Long position;
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
}

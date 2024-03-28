package de.gregord.drlleaderboardbackend.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Entity
@IdClass(CommunitySeason.CommunitySeasonId.class)
@Table(name = "community_seasons", indexes = {
        @Index(columnList = "seasonId"),
        @Index(columnList = "seasonIdName"),
        @Index(columnList = "track_id"),
        @Index(columnList = "excluded")
})
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommunitySeason {
    public static class CommunitySeasonId implements Serializable {
        private Integer seasonId;
        private Long track;
    }

    @Id
    @EqualsAndHashCode.Include
    private Integer seasonId;

    @Id
    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name = "track_id")
    private Track track;

    private String seasonIdName;
    private Integer customTrackDifficulty;
    private Boolean excluded = false;
}

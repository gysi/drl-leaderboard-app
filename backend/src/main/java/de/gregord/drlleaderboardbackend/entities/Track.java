package de.gregord.drlleaderboardbackend.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tracks", indexes = {
        @Index(columnList = "guid", unique = true),
        @Index(columnList = "mapId"),
        @Index(columnList = "mapName"),
        @Index(columnList = "parentCategory"),
        @Index(columnList = "name")
})
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Track {

    @Id
    @GeneratedValue(generator = TsidGenerator.GENERATOR_NAME)
    @GenericGenerator(name = TsidGenerator.GENERATOR_NAME, type = TsidGenerator.class)
    @EqualsAndHashCode.Include
    private Long id;
    @EqualsAndHashCode.Include
    private String guid;
    private String mapId;
    // custom
    private String mapName;
    //custom
    private String parentCategory;
    private String drlTrackId;
    private String name;
    // drl api name "is-drl-official"
    private Boolean isDrlOfficial;
    // drl api Name "is-public"
    private Boolean isPublic;
    // drl api name "map-distance"
    private Double mapDistance;
    // drl api name "map-mode-type"
    private String mapModeType;
    // drl api name "map-laps"
    private Integer mapLaps;
    // drl api name "map-difficulty"
    private Integer mapDifficulty;
    // drl api name "map-category"
    private String mapCategory;
    private String categories;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "track", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<LeaderboardEntry> leaderboardEntryEntries;

    public Track() {
    }

    public Track(String mapId, String guid, String trackId, String name) {
        this.mapId = mapId;
        this.guid = guid;
        this.drlTrackId = trackId;
        this.name = name;
    }
}

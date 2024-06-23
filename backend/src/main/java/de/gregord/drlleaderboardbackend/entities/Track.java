package de.gregord.drlleaderboardbackend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tracks", indexes = {
        @Index(columnList = "guid", unique = true),
        @Index(columnList = "mapId"),
        @Index(columnList = "mapName"),
        @Index(columnList = "parentCategory"),
        @Index(columnList = "name"),
        @Index(columnList = "mapCategoryId"),
        @Index(columnList = "ratingScore"),
        @Index(columnList = "ratingCount"),
        @Index(columnList = "trackCreator")}
)
@Getter
@Setter
@ToString
public class Track {

    @Id
    @GeneratedValue(generator = TsidGenerator.GENERATOR_NAME)
    @GenericGenerator(name = TsidGenerator.GENERATOR_NAME, type = TsidGenerator.class)
    private Long id;
    private String guid;
    private String mapId;
    // custom
    private String mapName;
    //custom
    private String parentCategory;
    private String drlTrackId;
    private String name;
    // drl api name "profile-name" sadly its only lowercase :(
    private String trackCreator;
    // drl api name "is-drl-official"
//    private Boolean isDrlOfficial;
    // drl api Name "is-public"
//    private Boolean isPublic;
    // drl api name "map-distance"
    private Double mapDistance;
    // drl api name "map-mode-type"
//    private String mapModeType;
    // drl api name "map-laps"
    private Integer mapLaps;
    // drl api name "map-difficulty"
    private Integer mapDifficulty;
    // drl api name "map-category"
    private String mapCategory;
    private Integer mapCategoryId;
    // drl api name "score"
    private Double ratingScore;
    // drl api name "rating-count"
    private Integer ratingCount;
    // drl api name "map-thumb"
    private String mapThumb;
//    private String categories;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "track", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<LeaderboardEntry> leaderboardEntryEntries;

    public Track() {
    }

    public Track(String mapId, String guid, String drlTrackId, String name, MapCategory parentCategory) {
        this.mapId = mapId;
        this.guid = guid;
        this.drlTrackId = drlTrackId;
        this.name = name;
        this.parentCategory = parentCategory.getDescription();
        this.mapCategoryId = parentCategory.ordinal();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy hp ? hp.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy hp ? hp.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Track track = (Track) o;
        return getId() != null && Objects.equals(getId(), track.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hp ? hp.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

package de.gregord.drlleaderboardbackend.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;

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
public class CommunitySeason {
    public static class CommunitySeasonId implements Serializable {
        private Integer seasonId;
        private Long track;

        @Override
        public final boolean equals(Object o) {
            if (this == o) return true;
            if (o == null) return false;
            Class<?> oEffectiveClass = o instanceof HibernateProxy ?
                    ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
            Class<?> thisEffectiveClass = this instanceof HibernateProxy ?
                    ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
            if (thisEffectiveClass != oEffectiveClass) return false;
            CommunitySeasonId that = (CommunitySeasonId) o;
            return seasonId != null && Objects.equals(seasonId, that.seasonId)
                    && track != null && Objects.equals(track, that.track);
        }

        @Override
        public final int hashCode() {
            return Objects.hash(seasonId, track);
        }
    }

    @Id
    private Integer seasonId;

    @Id
    @ManyToOne
    @JoinColumn(name = "track_id")
    private Track track;

    private String seasonIdName;
    private Integer customTrackDifficulty;
    private Boolean excluded = false;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ?
                ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        CommunitySeason that = (CommunitySeason) o;
        return getSeasonId() != null && Objects.equals(getSeasonId(), that.getSeasonId())
                && getTrack() != null && Objects.equals(getTrack(), that.getTrack());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(seasonId, track);
    }
}

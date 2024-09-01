package de.gregord.drlleaderboardbackend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "community_seasons_ranking_history")
public class CommunitySeasonRanking {
    @Id
    @CustomTsidGenerator
    private Long id;
    private Long seasonId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    private Double points;
    private Long position;
    private Double positionAverage;
    private Long positionBest;
    private Long invalidRuns;
    private Long completedTracks;
    private Long crashes;
    private Long penalties;
    private Long totalTime;
    private Double topSpeed;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy hp ? hp.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy hp ? hp.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        CommunitySeasonRanking that = (CommunitySeasonRanking) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hp ? hp.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

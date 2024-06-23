package de.gregord.drlleaderboardbackend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "players", indexes = {
        @Index(columnList = "playerId", unique = true),
        @Index(columnList = "profilePlatform"),
        @Index(columnList = "profilePlatformId"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "updatedAt"),
})
@Getter
@Setter
@ToString
public class Player {
    @Id
    @CustomTsidGenerator
    private Long id;
    private String playerName;
    private String profileThumb;
    private String flagUrl;
    private String playerId;
    private String profilePlatform;
    private String profilePlatformId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static boolean equalsForUpdate(Player thiz, Player that) {
        if (thiz == that) return true;

        if (thiz == null || that == null) return false;

        return new EqualsBuilder()
                .append(thiz.id, that.id)
                .append(thiz.playerName, that.playerName)
                .append(thiz.profileThumb, that.profileThumb)
                .append(thiz.flagUrl, that.flagUrl)
                .append(thiz.profilePlatform, that.profilePlatform)
                .append(thiz.profilePlatformId, that.profilePlatformId)
                .isEquals();
    }

    public static Player simpleCopy(Player toCopy) {
        if (toCopy == null) {
            return null;
        }
        Player player = new Player();
        player.id = toCopy.id;
        player.playerId = toCopy.playerId;
        player.playerName = toCopy.playerName;
        player.profileThumb = toCopy.profileThumb;
        player.flagUrl = toCopy.flagUrl;
        player.profilePlatform = toCopy.profilePlatform;
        player.profilePlatformId = toCopy.profilePlatformId;
        player.createdAt = toCopy.createdAt;
        player.updatedAt = toCopy.updatedAt;
        return player;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy hp ? hp.getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy hp ?
                hp.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Player player = (Player) o;
        return getId() != null && Objects.equals(getId(), player.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hp ? hp.getHibernateLazyInitializer().getPersistentClass().hashCode() :
                getClass().hashCode();
    }
}

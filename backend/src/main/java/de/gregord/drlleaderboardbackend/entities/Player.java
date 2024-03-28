package de.gregord.drlleaderboardbackend.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Player {
    @Id
    @GeneratedValue(generator = TsidGenerator.GENERATOR_NAME)
    @GenericGenerator(name = TsidGenerator.GENERATOR_NAME, type = TsidGenerator.class)
    @EqualsAndHashCode.Include
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
}

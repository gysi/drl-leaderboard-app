package de.gregord.drlleaderboardbackend.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
}

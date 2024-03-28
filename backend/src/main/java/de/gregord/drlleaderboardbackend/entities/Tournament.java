package de.gregord.drlleaderboardbackend.entities;

import de.gregord.drlleaderboardbackend.entities.tournament.TournamentRanking;
import de.gregord.drlleaderboardbackend.entities.tournament.TournamentRound;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JavaType;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.hibernate.type.descriptor.jdbc.JsonJdbcType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tournaments", indexes = {
        @Index(columnList = "guid", unique = true),
        @Index(columnList = "drlId"),
        @Index(columnList = "title"),
        @Index(columnList = "map"),
        @Index(columnList = "customMap"),
        @Index(columnList = "customMapTitle"),
        @Index(columnList = "playerIds"),
        @Index(columnList = "rankings"),
        @Index(columnList = "rounds"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "updatedAt"),
        @Index(columnList = "registrationStartAt"),
        @Index(columnList = "registrationEndAt"),
        @Index(columnList = "nextTurnAt"),
        @Index(columnList = "completedAt"),
})
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Tournament {
    @Id
    @GeneratedValue(generator = TsidGenerator.GENERATOR_NAME)
    @GenericGenerator(name = TsidGenerator.GENERATOR_NAME, type = TsidGenerator.class)
    @EqualsAndHashCode.Include
    private Long id;

    private String drlId; // drl api name "id"
    private String guid; // drl api name "guid"
    private String title; // drl api name "title"
    private String description; // drl api name "description"
    private String imageUrl; // drl api name "image-url"
    private String map; // drl api name "map"
    private String customMap; // drl api name "custom-map"
    private String customMapTitle; // drl api name "custom-map-title"
    private String track; // drl api name "track", seems to be null

    private String status; // drl api name "status": 'idle', 'active', complete'

    private String region; // drl api name "region"
    private Boolean isPrivate; // drl api name "private"
    private Boolean isTestTournament;

    private int playersSize; // drl api name "players-size"
    private String playersPerMatch; // drl api name "players-per-match"
    private String winnersPerMatch; // drl api name "winners-per-match"
    private String heatsPerMatch; // drl api name "heats-per-match"

    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> playerIds; // drl api name "player-ids"

    @JdbcTypeCode(SqlTypes.JSON)
    private List<TournamentRanking> rankings; // drl api name "ranking"

    @JdbcTypeCode(SqlTypes.JSON)
    private List<TournamentRound> rounds; // drl api name "rounds"

    private LocalDateTime createdAt; // drl api name "created-at"
    private LocalDateTime updatedAt; // drl api name "updated-at"

    private LocalDateTime registrationStartAt; // drl api name "register-start"
    private LocalDateTime registrationEndAt; // drl api name "register-end"

    private LocalDateTime nextTurnAt; // drl api name "next-turn", null if tournament is over
    private LocalDateTime completedAt; // drl api name "completed-at", does not exist if tournament is not completed
}

package de.gregord.drlleaderboardbackend.entities;

import de.gregord.drlleaderboardbackend.entities.tournament.TournamentRanking;
import de.gregord.drlleaderboardbackend.entities.tournament.TournamentRound;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Tournament {
    @Id
    @GeneratedValue(generator = TsidGenerator.GENERATOR_NAME)
    @GenericGenerator(name = TsidGenerator.GENERATOR_NAME, strategy = TsidGenerator.STRATEGY_NAME)
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
    private String trackId; // drl api name "track", seems to be null

    private String status; // drl api name "status": 'idle', 'complete'

    private String region; // drl api name "region"
    private Boolean isPrivate; // drl api name "private"

    private int playersPerMatch; // drl api name "players-per-match"
    private int winnerPerMatch; // drl api name "winners-per-match"
    private int heatsPerMatch; // drl api name "heats-per-match"

    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> playerIds; // drl api name "player-ids"

    @JdbcTypeCode(SqlTypes.JSON)
    private List<TournamentRanking> rankings; // drl api name "ranking"

    @JdbcTypeCode(SqlTypes.JSON)
    private List<TournamentRound> matches; // drl api name "matches"

    private LocalDateTime createdAt; // drl api name "created-at"
    private LocalDateTime updatedAt; // drl api name "updated-at"

    private LocalDateTime registrationStartAt; // drl api name "register-start"
    private LocalDateTime registrationEndAt; // drl api name "register-end"

    private LocalDateTime nextTurnAt; // drl api name "next-turn", null if tournament is over
    private LocalDateTime completedAt; // drl api name "completed-at", does not exist if tournament is not completed
}

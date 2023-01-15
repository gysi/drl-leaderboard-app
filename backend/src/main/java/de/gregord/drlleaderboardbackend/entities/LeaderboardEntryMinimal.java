package de.gregord.drlleaderboardbackend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "leaderboards")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class LeaderboardEntryMinimal {
    @Id
    @GeneratedValue(generator = TsidGenerator.GENERATOR_NAME)
    @GenericGenerator(name = TsidGenerator.GENERATOR_NAME, strategy = TsidGenerator.STRATEGY_NAME)
    @EqualsAndHashCode.Include
    private Long id;
    private String playerId;
    private String playerName;
    private Long score;
    private Long position;
    private Double points;
    // from drl api
    private LocalDateTime createdAt;
}

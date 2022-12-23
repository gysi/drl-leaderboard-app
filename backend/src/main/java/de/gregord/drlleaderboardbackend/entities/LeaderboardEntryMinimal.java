package de.gregord.drlleaderboardbackend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
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
public class LeaderboardEntryMinimal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String playerId;
    private String playerName;
    private Long score;
    private Long position;
    private Double points;
    // from drl api
    private LocalDateTime createdAt;
//    @ToString.Exclude
//    @ManyToMany(mappedBy = "beatenBy", fetch = FetchType.LAZY)
//    private List<LeaderboardEntry> beats = new ArrayList<>();
}

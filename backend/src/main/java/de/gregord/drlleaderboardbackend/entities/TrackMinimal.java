package de.gregord.drlleaderboardbackend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "tracks")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class TrackMinimal {
    @Id
    @GeneratedValue(generator = TsidGenerator.GENERATOR_NAME)
    @GenericGenerator(name = TsidGenerator.GENERATOR_NAME, strategy = TsidGenerator.STRATEGY_NAME)
    @EqualsAndHashCode.Include
    private Long id;
    private String mapName;
    private String parentCategory;
    private String name;
}

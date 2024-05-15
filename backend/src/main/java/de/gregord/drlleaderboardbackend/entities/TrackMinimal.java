package de.gregord.drlleaderboardbackend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Entity
@Table(name = "tracks")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TrackMinimal {
    @Id
    @GeneratedValue(generator = TsidGenerator.GENERATOR_NAME)
    @GenericGenerator(name = TsidGenerator.GENERATOR_NAME, type = TsidGenerator.class)
    private Long id;
    private String mapName;
    private String parentCategory;
    private String name;
    private Integer mapCategoryId;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        TrackMinimal that = (TrackMinimal) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

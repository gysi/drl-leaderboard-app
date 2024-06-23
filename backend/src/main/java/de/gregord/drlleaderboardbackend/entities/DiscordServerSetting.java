package de.gregord.drlleaderboardbackend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "discord_server_settings")
@Getter
@Setter
@ToString
public class DiscordServerSetting {
    @Id
    @CustomTsidGenerator
    private Long id;

    private Long serverId;

    @Enumerated(EnumType.STRING)
    private Settings setting;

    private String value;

    public enum Settings {
        TAG_ROLE
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
        DiscordServerSetting that = (DiscordServerSetting) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hp ? hp.getHibernateLazyInitializer().getPersistentClass().hashCode() :
                getClass().hashCode();
    }
}


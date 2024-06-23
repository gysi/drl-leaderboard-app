package de.gregord.drlleaderboardbackend.entities;

import de.gregord.drlleaderboardbackend.services.discord.DiscordPostType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "discord_active_channels")
@Getter
@Setter
@ToString
public class DiscordActiveChannels {
    @Id
    @CustomTsidGenerator
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "server_id", nullable = false)
    private DiscordServer discordServer;
    private String channelId;
    private String channelName;
    @Enumerated(EnumType.STRING)
    private DiscordPostType postType;
    private LocalDateTime lastPostAt;

    public DiscordActiveChannels() {

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
        DiscordActiveChannels that = (DiscordActiveChannels) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hp ? hp.getHibernateLazyInitializer().getPersistentClass().hashCode() :
                getClass().hashCode();
    }
}

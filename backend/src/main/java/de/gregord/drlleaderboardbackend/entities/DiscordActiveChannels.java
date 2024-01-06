package de.gregord.drlleaderboardbackend.entities;

import de.gregord.drlleaderboardbackend.services.discord.DiscordPostType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "discord_active_channels")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DiscordActiveChannels {

    @Id
    @GeneratedValue(generator = TsidGenerator.GENERATOR_NAME)
    @GenericGenerator(name = TsidGenerator.GENERATOR_NAME, strategy = TsidGenerator.STRATEGY_NAME)
    @EqualsAndHashCode.Include
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
}

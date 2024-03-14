package de.gregord.drlleaderboardbackend.entities;

import de.gregord.drlleaderboardbackend.services.discord.DiscordBotType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "discord_servers")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DiscordServer {

    @Id
    @GeneratedValue(generator = TsidGenerator.GENERATOR_NAME)
    @GenericGenerator(name = TsidGenerator.GENERATOR_NAME, type = TsidGenerator.class)
    @EqualsAndHashCode.Include
    private Long id;

    private String serverId;
    private String serverName;

    @Enumerated(EnumType.STRING)
    private DiscordBotType botType;

    public DiscordServer() {

    }
}

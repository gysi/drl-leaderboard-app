package de.gregord.drlleaderboardbackend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    @GenericGenerator(name = TsidGenerator.GENERATOR_NAME, strategy = TsidGenerator.STRATEGY_NAME)
    @EqualsAndHashCode.Include
    private Long id;

    private String serverId;
    private String serverName;

    public DiscordServer() {

    }
}

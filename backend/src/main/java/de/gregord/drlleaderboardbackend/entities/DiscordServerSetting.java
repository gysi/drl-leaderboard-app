package de.gregord.drlleaderboardbackend.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "discord_server_settings")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DiscordServerSetting {
//    id bigint not null primary key,
//    server_id varchar(255) not null,
//    setting varchar(255) not null,
//    value varchar(255)
    @Id
    @GeneratedValue(generator = TsidGenerator.GENERATOR_NAME)
    @GenericGenerator(name = TsidGenerator.GENERATOR_NAME, type = TsidGenerator.class)
    @EqualsAndHashCode.Include
    private Long id;

    private Long serverId;

    @Enumerated(EnumType.STRING)
    private Settings setting;

    private String value;

    public enum Settings {
        TAG_ROLE
    }
}


package de.gregord.drlleaderboardbackend.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
        value = "de.gregord.drlleaderboardbackend.repositories"
)
@EntityScan("de.gregord.drlleaderboardbackend.entities")
public class JpaConfig {

}

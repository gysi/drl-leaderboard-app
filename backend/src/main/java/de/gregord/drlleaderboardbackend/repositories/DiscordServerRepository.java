package de.gregord.drlleaderboardbackend.repositories;

import de.gregord.drlleaderboardbackend.entities.DiscordServer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscordServerRepository extends JpaRepository<DiscordServer, Long> {
    Optional<DiscordServer> findByServerId(String serverId);
}

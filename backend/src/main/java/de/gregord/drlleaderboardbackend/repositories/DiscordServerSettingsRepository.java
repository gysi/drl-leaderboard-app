package de.gregord.drlleaderboardbackend.repositories;

import de.gregord.drlleaderboardbackend.entities.DiscordServerSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscordServerSettingsRepository extends JpaRepository<DiscordServerSetting, Long> {

    Optional<DiscordServerSetting> findByServerIdAndSetting(Long serverId, DiscordServerSetting.Settings settings);
}

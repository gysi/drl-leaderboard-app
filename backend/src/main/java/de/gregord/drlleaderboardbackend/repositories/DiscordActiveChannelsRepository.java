package de.gregord.drlleaderboardbackend.repositories;

import de.gregord.drlleaderboardbackend.entities.DiscordActiveChannels;
import de.gregord.drlleaderboardbackend.entities.DiscordServer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiscordActiveChannelsRepository extends JpaRepository<DiscordActiveChannels, Long> {
    List<DiscordActiveChannels> findByDiscordServer(DiscordServer discordServer);
    List<DiscordActiveChannels> findByDiscordServerAndPostType(DiscordServer discordServer, String postType);
    Optional<DiscordActiveChannels> findByChannelId(String channelId);
    List<DiscordActiveChannels> findByPostType(String postType);
}

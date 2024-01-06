package de.gregord.drlleaderboardbackend.repositories;

import de.gregord.drlleaderboardbackend.entities.DiscordActiveChannels;
import de.gregord.drlleaderboardbackend.entities.DiscordServer;
import de.gregord.drlleaderboardbackend.services.discord.DiscordPostType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiscordActiveChannelsRepository extends JpaRepository<DiscordActiveChannels, Long> {
    List<DiscordActiveChannels> findByDiscordServer(DiscordServer discordServer);
    List<DiscordActiveChannels> findByDiscordServerAndPostType(DiscordServer discordServer, DiscordPostType postType);
    Optional<DiscordActiveChannels> findByChannelIdAndPostType(String channelId, DiscordPostType postType);
    List<DiscordActiveChannels> findByPostType(DiscordPostType postType);
}

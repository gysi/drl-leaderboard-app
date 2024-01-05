package de.gregord.drlleaderboardbackend.services.discord;

import de.gregord.drlleaderboardbackend.entities.DiscordActiveChannels;
import de.gregord.drlleaderboardbackend.entities.DiscordServer;
import de.gregord.drlleaderboardbackend.repositories.DiscordActiveChannelsRepository;
import de.gregord.drlleaderboardbackend.repositories.DiscordServerRepository;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.guild.GuildDeleteEvent;
import discord4j.core.object.entity.Guild;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
public class DiscordServerManagementService {
    private static final Logger LOG = LoggerFactory.getLogger(DiscordServerManagementService.class);
    private final DiscordInitializationService discordInitializationService;
    private final DiscordServerRepository discordServerRepository;
    private final DiscordActiveChannelsRepository discordActiveChannelsRepository;

    public DiscordServerManagementService(
            DiscordInitializationService discordInitializationService,
            DiscordServerRepository discordServerRepository,
            DiscordActiveChannelsRepository discordActiveChannelsRepository
    ) {
        this.discordInitializationService = discordInitializationService;
        this.discordServerRepository = discordServerRepository;
        this.discordActiveChannelsRepository = discordActiveChannelsRepository;
    }

    @PostConstruct
    public void init() {
        discordInitializationService.getGateway().thenAccept(gateway -> {
            handleGuildCreateEvents(gateway).subscribe();
            handleGuildDeleteEvents(gateway).subscribe();
        });
    }

    private Mono<Void> handleGuildCreateEvents(GatewayDiscordClient gateway) {
        return gateway.on(GuildCreateEvent.class, event -> {
            Snowflake guildId = event.getGuild().getId();
            String guildName = event.getGuild().getName();
            LOG.info("Received GuildCreateEvent for guild: {} ({})", guildName, guildId.asString());
            return checkAndCreateServer(guildId.asString(), guildName);
        }).then();
    }

    private Mono<Void> checkAndCreateServer(String guildId, String guildName) {
        return Mono.fromCallable(() -> discordServerRepository.findByServerId(guildId))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalDiscordServer -> {
                    if (optionalDiscordServer.isEmpty()) {
                        return createNewServer(guildId, guildName)
                                .doOnSuccess(unused -> LOG.info("Successfully created new Server on GuildCreateEvent for guild: {} ({})", guildName, guildId))
                                .doOnError(error -> LOG.error("Error creating new Server while processing GuildCreateEvent for guild: {} ({}): {}", guildName, guildId, error.getMessage()));
                    }
                    LOG.info("Server already exists on GuildCreateEvent for guild: {} ({})", guildName, guildId);
                    return Mono.empty();
                });
    }

    private Mono<Void> createNewServer(String guildId, String guildName) {
        return Mono.fromRunnable(() -> {
            DiscordServer newServer = new DiscordServer();
            newServer.setServerId(guildId);
            newServer.setServerName(guildName);
            discordServerRepository.save(newServer);
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    private Mono<Void> handleGuildDeleteEvents(GatewayDiscordClient gateway) {
        return gateway.on(GuildDeleteEvent.class, event -> {
            if (!event.isUnavailable()) {
                String guildId = event.getGuildId().asString();
                String guildName = event.getGuild().map(Guild::getName).orElse("Unknown");
                return removeServer(guildId)
                        .doOnSuccess(unused -> LOG.info("Successfully removed Server on GuildDeleteEvent for guild: {} ({})", guildName, guildId))
                        .doOnError(error -> LOG.error("Error removing Server while processing GuildDeleteEvent for guild: {} ({}): {}", guildName, guildId, error.getMessage()));
            }
            return Mono.empty();
        }).then();
    }

    private Mono<Void> removeServer(String guildId) {
        return Mono.fromRunnable(() -> {
            discordServerRepository.findByServerId(guildId).ifPresent(server -> {
                List<DiscordActiveChannels> activeChannels = discordActiveChannelsRepository.findByDiscordServer(server);
                discordActiveChannelsRepository.deleteAll(activeChannels);
                discordServerRepository.delete(server);
            });
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }
}

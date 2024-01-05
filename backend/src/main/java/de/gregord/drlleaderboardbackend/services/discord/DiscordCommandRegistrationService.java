package de.gregord.drlleaderboardbackend.services.discord;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.command.ApplicationCommand;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DiscordCommandRegistrationService {
    public static final Logger LOG = LoggerFactory.getLogger(DiscordCommandRegistrationService.class);
    DiscordInitializationService discordInitializationService;

    public DiscordCommandRegistrationService(DiscordInitializationService discordInitializationService) {
        this.discordInitializationService = discordInitializationService;
    }

    @PostConstruct
    public void init() {
        discordInitializationService.getGateway().thenAccept(gateway -> {
//            registerSlashCommandsOnReady(gateway).subscribe();
            handleGuildCreateEvents(gateway).subscribe();
        });
    }

    // Maybe remove this in the future, it seems like the GuildCreateEvent is enough, because it is also triggered on startup
//    private Mono<Void> registerSlashCommandsOnReady(GatewayDiscordClient gateway) {
//        return gateway.on(ReadyEvent.class, event -> {
//            event.getGuilds().forEach(guild -> {
//                registerSlashCommandForGuild(gateway, guild.getId(), "drl-leaderboard-posts-activate", "Activate leaderboard posts");
//                registerSlashCommandForGuild(gateway, guild.getId(), "drl-leaderboard-posts-deactivate", "Deactivate leaderboard posts");
//            });
//            return Mono.empty();
//        }).then();
//    }

    private Mono<Void> handleGuildCreateEvents(GatewayDiscordClient gateway) {
        return gateway.on(GuildCreateEvent.class, event -> {
            Snowflake guildId = event.getGuild().getId();
            String guildName = event.getGuild().getName();
            LOG.info("Received GuildCreateEvent for guild: {} ({})", guildName, guildId.asString());
            // Register commands for the new guild
            registerSlashCommandForGuild(gateway, guildId, "drl-leaderboard-posts-activate", "Activate leaderboard posts");
            registerSlashCommandForGuild(gateway,guildId, "drl-leaderboard-posts-deactivate", "Deactivate leaderboard posts");
            return Mono.empty();
        }).then();
    }

    private void registerSlashCommandForGuild(GatewayDiscordClient gateway, Snowflake guildId, String commandName, String description) {
        ApplicationCommandOptionData channelOption = ApplicationCommandOptionData.builder()
                .name("channel")
                .description("Select a channel")
                .type(ApplicationCommandOption.Type.STRING.getValue())
                .required(true)
                .autocomplete(true)
                .build();

        gateway.getRestClient().getApplicationService()
                .createGuildApplicationCommand(gateway.getRestClient().getApplicationId().block(), guildId.asLong(),
                        ApplicationCommandRequest.builder()
                                .name(commandName)
                                .description(description)
                                .dmPermission(false)
                                .defaultMemberPermissions("0")
                                .type(ApplicationCommand.Type.CHAT_INPUT.getValue())
                                .addOption(channelOption)
                                .build())
                .subscribe(
                        result -> LOG.info("Slash command '{}' registered for guild: {}", commandName, guildId.asString()),
                        error -> LOG.error("Error registering slash command '{}' for guild: {}", commandName, guildId.asString(), error)
                );
    }
}

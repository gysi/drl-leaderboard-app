package de.gregord.drlleaderboardbackend.services.discord;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.guild.GuildCreateEvent;
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
        discordInitializationService.getLeaderboardGateway().thenAccept(gateway -> {
//            registerSlashCommandsOnReady(gateway).subscribe();
            handleGuildCreateEvents(gateway, DiscordBotType.LEADERBOARD).subscribe();
        });

        discordInitializationService.getTournamentGateway().thenAccept(gateway -> {
//            registerSlashCommandsOnReady(gateway).subscribe();
            handleGuildCreateEvents(gateway, DiscordBotType.TOURNAMENT).subscribe();
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

    private Mono<Void> handleGuildCreateEvents(GatewayDiscordClient gateway, DiscordBotType botType) {
        return gateway.on(GuildCreateEvent.class, event -> {
            Snowflake guildId = event.getGuild().getId();
            long applicationId = gateway.getRestClient().getApplicationId().block();

            // Delete all existing commands
            return gateway.getRestClient().getApplicationService()
                    .getGuildApplicationCommands(applicationId, guildId.asLong())
                    .flatMap(command -> {
                        String commandName = command.name();
                        return gateway.getRestClient().getApplicationService()
                                .deleteGuildApplicationCommand(applicationId, guildId.asLong(), command.id().asLong())
                                .doOnSuccess(unused -> LOG.info("Deleted old command: {}", commandName))
                                .doOnError(error -> LOG.error("Error deleting old command: {}", commandName, error));
                    })
                    .doOnComplete(() -> {
                        botType.getCommands().forEach(command -> {
                            registerSlashCommandForGuild(gateway, guildId, command.getCommandName(), command.getDescription());
                        });
                    }).then();
        }).then();
    }

    private void registerSlashCommandForGuild(GatewayDiscordClient gateway, Snowflake guildId, String commandName, String description) {
        ApplicationCommandOptionData channelOption = ApplicationCommandOptionData.builder()
                .name("channel")
                .description("Select a channel to which the bot should post the messages")
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

package de.gregord.drlleaderboardbackend.services.discord;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.object.command.ApplicationCommand;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.discordjson.json.ImmutableApplicationCommandRequest;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

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
            handleGuildCreateEvents(gateway, DiscordBotType.LEADERBOARD).subscribe();
        });

        discordInitializationService.getTournamentGateway().thenAccept(gateway -> {
            handleGuildCreateEvents(gateway, DiscordBotType.TOURNAMENT).subscribe();
        });
    }

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
                            registerSlashCommandForGuild(gateway, guildId, command);
                        });
                    }).then();
        }).then();
    }

    private void registerSlashCommandForGuild(GatewayDiscordClient gateway, Snowflake guildId, DiscordCommand command) {
        String commandName = command.getCommandName();
        String commandDescription = command.getDescription();
        List<ApplicationCommandOptionData> channelOptions = new ArrayList<>();
        if(command.activatesChannel() || command.deactivatesChannel()) {
            channelOptions.add(ApplicationCommandOptionData.builder()
                    .name("channel")
                    .description("Select a channel to which the bot should post the messages")
                    .type(ApplicationCommandOption.Type.STRING.getValue())
                    .required(true)
                    .autocomplete(true)
                    .build()
            );
        } else if (command.setsSettingRegardingRoles()) {
            channelOptions.add(ApplicationCommandOptionData.builder()
                    .name("role")
                    .description("Selects a role that will be tagged for a tournament reminder")
                    .type(ApplicationCommandOption.Type.ROLE.getValue())
                    .required(true)
                    .autocomplete(true)
                    .build()
            );
        }

        ImmutableApplicationCommandRequest.Builder commandRequestBuilder = ApplicationCommandRequest.builder()
                .name(commandName)
                .description(commandDescription)
                .dmPermission(false)
                .defaultMemberPermissions("0")
                .type(ApplicationCommand.Type.CHAT_INPUT.getValue());
        if(!channelOptions.isEmpty()) {
            commandRequestBuilder.addAllOptions(channelOptions);
        }

        ImmutableApplicationCommandRequest commandRequest = commandRequestBuilder.build();

        gateway.getRestClient().getApplicationService()
                .createGuildApplicationCommand(gateway.getRestClient().getApplicationId().block(), guildId.asLong(),
                        commandRequest)
                .subscribe(
                        result -> LOG.info("Slash command '{}' registered for guild: {}", commandName, guildId.asString()),
                        error -> LOG.error("Error registering slash command '{}' for guild: {}", commandName, guildId.asString(), error)
                );
    }
}

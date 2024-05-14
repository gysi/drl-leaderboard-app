package de.gregord.drlleaderboardbackend.services.discord;

import de.gregord.drlleaderboardbackend.entities.DiscordActiveChannels;
import de.gregord.drlleaderboardbackend.entities.DiscordServer;
import de.gregord.drlleaderboardbackend.entities.DiscordServerSetting;
import de.gregord.drlleaderboardbackend.entities.Tournament;
import de.gregord.drlleaderboardbackend.entities.tournament.TournamentRound;
import de.gregord.drlleaderboardbackend.repositories.DiscordActiveChannelsRepository;
import de.gregord.drlleaderboardbackend.repositories.DiscordServerRepository;
import de.gregord.drlleaderboardbackend.repositories.DiscordServerSettingsRepository;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.channel.GuildChannel;
import jakarta.annotation.PostConstruct;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DiscordCommandService {
    private static final Logger LOG = LoggerFactory.getLogger(DiscordCommandService.class);
    private final DiscordInitializationService discordInitializationService;
    private final DiscordServerRepository discordServerRepository;
    private final DiscordActiveChannelsRepository discordActiveChannelsRepository;
    private final DiscordServerSettingsRepository discordServerSettingsRepository;
    private final DiscordMessageService discordMessageService;

    public DiscordCommandService(
            DiscordInitializationService discordInitializationService,
            DiscordServerRepository discordServerRepository,
            DiscordActiveChannelsRepository discordActiveChannelsRepository,
            DiscordServerSettingsRepository discordServerSettingsRepository,
            DiscordMessageService discordMessageService
    ) {
        this.discordInitializationService = discordInitializationService;
        this.discordServerRepository = discordServerRepository;
        this.discordActiveChannelsRepository = discordActiveChannelsRepository;
        this.discordServerSettingsRepository = discordServerSettingsRepository;
        this.discordMessageService = discordMessageService;
    }

    @PostConstruct
    public void init() {
        discordInitializationService.getLeaderboardGateway().thenAccept(gateway -> {
            handleChatInputInteractionEvents(gateway, DiscordBotType.LEADERBOARD).subscribe();
        });

        discordInitializationService.getTournamentGateway().thenAccept(gateway -> {
            handleChatInputInteractionEvents(gateway, DiscordBotType.TOURNAMENT).subscribe();
        });
    }

    private Mono<Void> handleChatInputInteractionEvents(GatewayDiscordClient gateway, DiscordBotType botType) {
        return gateway.on(ChatInputInteractionEvent.class, event -> {
            String commandName = event.getCommandName();
            String guildId = event.getInteraction().getGuildId().map(Snowflake::asString).orElse("Unknown Guild");
            String channelId = event.getInteraction().getChannelId().asString();

            LOG.info("Received ChatInputInteractionEvent: Command = '{}', Guild ID = '{}', Channel ID = '{}'",
                    commandName, guildId, channelId);

            DiscordCommand discordCommand = DiscordCommand.fromCommandName(commandName);
            if (discordCommand == null || !botType.getCommands().contains(discordCommand)) {
                LOG.info("Command '{}' is not supported by this bot type: {}", commandName, botType);
                return Mono.empty();
            }
            if (discordCommand.activatesChannel()) {
                return handleActivateCommand(gateway, botType, event, discordCommand.getPostType());
            } else if (discordCommand.deactivatesChannel()) {
                return handleDeactivateCommand(event, discordCommand.getPostType());
            } else if (discordCommand.setsSettingRegardingRoles()) {
                return handleSetSettingsRegardingRoles(event, botType, discordCommand);
            } else if (discordCommand.removesSetting()) {
                return handleRemoveSetting(event, botType, discordCommand);
            } else if (discordCommand.isTestCommand()) {
                return handleTestCommand(event, botType, discordCommand);
            }
            return Mono.empty();
        }).then();
    }

    private Mono<Void> handleActivateCommand(GatewayDiscordClient gateway, DiscordBotType botType,
                                             ChatInputInteractionEvent event, DiscordPostType postType) {
        String guildId = event.getInteraction().getGuildId().orElseThrow().asString();
        String channelId = event.getOption("channel")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);

        if (channelId == null) {
            return event.reply("No channel selected").withEphemeral(true);
        }

        return saveSelectedChannel(gateway, botType, guildId, channelId, postType)
                .then(Mono.defer(() -> event.reply("Channel activated for leaderboard posts").withEphemeral(true)))
                .onErrorResume(e -> {
                    LOG.error("Error activating channel: {}", e.getMessage());
                    return event.reply("Failed to activate channel due to an error").withEphemeral(true);
                });
    }

    private Mono<Void> handleDeactivateCommand(ChatInputInteractionEvent event, DiscordPostType postType) {
        String channelId = event.getOption("channel")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);

        if (channelId == null) {
            return event.reply("No channel selected").withEphemeral(true);
        }

        return removeSelectedChannel(channelId, postType)
                .then(Mono.defer(() -> event.reply("Channel deactivated for posts").withEphemeral(true)))
                .onErrorResume(e -> {
                    LOG.error("Error deactivating channel: {}", e.getMessage());
                    return event.reply("Failed to deactivate channel due to an error").withEphemeral(true);
                });
    }

    private Mono<Void> handleSetSettingsRegardingRoles(ChatInputInteractionEvent event,
                                                       DiscordBotType botType, DiscordCommand discordCommand) {
        String guildId = event.getInteraction().getGuildId().orElseThrow().asString();
        Role role = event.getOption("role")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asRole)
                .map(Mono::block)
                .orElse(null);

        if (role == null) {
            return event.reply("No role selected or role not found").withEphemeral(true);
        }

        return saveRoleTagSetting(guildId, botType, discordCommand, role)
                .then(Mono.defer(() -> event.reply("Role tagging activated for tournament reminders using the role " + role.getName()).withEphemeral(true)))
                .onErrorResume(e -> {
                    LOG.error("Error deactivating channel: {}", e.getMessage());
                    return event.reply("Failed to deactivate channel due to an error").withEphemeral(true);
                });
    }

    private Mono<Void> handleRemoveSetting(ChatInputInteractionEvent event, DiscordBotType botType, DiscordCommand discordCommand) {
        String guildId = event.getInteraction().getGuildId().orElseThrow().asString();

        return Mono.fromRunnable(() -> {
            DiscordServer server = discordServerRepository.findByServerIdAndBotType(guildId, botType)
                    .orElseThrow(() -> new IllegalStateException("Server not found: " + guildId));

            discordServerSettingsRepository.findByServerIdAndSetting(server.getId(), discordCommand.getSetting())
                    .ifPresent(discordServerSettingsRepository::delete);
        }).subscribeOn(Schedulers.boundedElastic())
                .then(Mono.defer(() -> event.reply("Setting removed").withEphemeral(true)))
                .onErrorResume(e -> {
                    LOG.error("Error removing setting: {}", e.getMessage());
                    return event.reply("Failed to remove setting due to an error").withEphemeral(true);
                });
    }

    private Mono<Void> saveRoleTagSetting(String guildId, DiscordBotType botType, DiscordCommand discordCommand,
                                          Role role) {
        return Mono.fromRunnable(() -> {
            DiscordServer server = discordServerRepository.findByServerIdAndBotType(guildId, botType)
                    .orElseThrow(() -> new IllegalStateException("Server not found: " + guildId));

            Optional<DiscordServerSetting> existingSetting = discordServerSettingsRepository.findByServerIdAndSetting(
                    server.getId(), DiscordServerSetting.Settings.TAG_ROLE);

            String roleIdOrTag;
            if(role.getName().contains("@")) {
                roleIdOrTag = role.getName();
            } else {
                roleIdOrTag = role.getId().asString();
            }

            DiscordServerSetting discordServerSetting;
            if (existingSetting.isPresent()) {
                discordServerSetting = existingSetting.get();
                discordServerSetting.setValue(roleIdOrTag);
            } else {
                discordServerSetting = new DiscordServerSetting();
                discordServerSetting.setServerId(server.getId());
                discordServerSetting.setSetting(DiscordServerSetting.Settings.TAG_ROLE);
                discordServerSetting.setValue(roleIdOrTag);
            }

            discordServerSettingsRepository.save(discordServerSetting);
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    private Mono<Void> handleTestCommand(ChatInputInteractionEvent event, DiscordBotType botType, DiscordCommand discordCommand) {
        return Mono.defer(() -> {
            Mono<DiscordServer> serverMono = Mono.fromCallable(() ->
                    discordServerRepository.findByServerIdAndBotType(event.getInteraction().getGuildId().orElseThrow().asString(), botType)
                            .orElseThrow(() -> new IllegalStateException("Server not found"))
            ).subscribeOn(Schedulers.boundedElastic());

            if (discordCommand == DiscordCommand.TOURNAMENT_REMINDER_TEST) {
                return handleTournamentReminderTest(event, serverMono);
            } else if (discordCommand == DiscordCommand.TOURNAMENT_RESULTS_TEST) {
                return handleTournamentResultTest(event, serverMono);
            } else {
                return Mono.empty();
            }
        });
    }

    private Mono<Void> handleTournamentReminderTest(ChatInputInteractionEvent event, Mono<DiscordServer> serverMono) {
        return serverMono.flatMap(server ->
                        Mono.fromCallable(() -> discordActiveChannelsRepository.findByPostType(DiscordPostType.TOURNAMENT_REMINDER))
                                .subscribeOn(Schedulers.boundedElastic())
                                .flatMap(channels -> {
                                    if (channels.isEmpty()) {
                                        return Mono.error(new IllegalStateException("No channels activated for tournament reminders"));
                                    } else {
                                        Tournament testTournament = DiscordTestData.createTestTournamentForTournamentReminder30Min();
                                        return discordMessageService.sendMessageToTournamentReminderChannelFlux(testTournament, false, true)
                                                .thenMany(discordMessageService.sendMessageToTournamentReminderChannelFlux(testTournament, true, true))
                                                .then();
                                    }
                                })
                )
                .publishOn(Schedulers.boundedElastic())
                .doOnSuccess(unused -> {
                    event.reply("Test successful").subscribe();
                })
                .doOnError(error -> {
                    event.reply("Test failed: " + error.getMessage()).subscribe();
                });
    }

    private Mono<Void> handleTournamentResultTest(ChatInputInteractionEvent event, Mono<DiscordServer> serverMono) {
        return serverMono.flatMap(server ->
                        Mono.fromCallable(() -> discordActiveChannelsRepository.findByPostType(DiscordPostType.TOURNAMENT_RESULTS))
                                .subscribeOn(Schedulers.boundedElastic())
                                .flatMap(channels -> {
                                    if (channels.isEmpty()) {
                                        return Mono.error(new IllegalStateException("No channels activated for tournament results"));
                                    } else {
                                        Tournament testTournament = DiscordTestData.createTestTournamentFinished();
                                        return discordMessageService.sendMessageToTournamentResultChannelFlux(testTournament, true)
                                                .then();
                                    }
                                })
                )
                .publishOn(Schedulers.boundedElastic())
                .doOnSuccess(unused -> {
                    event.reply("Test successful").subscribe();
                })
                .doOnError(error -> {
                    event.reply("Test failed: " + error.getMessage()).subscribe();
                });
    }

    private Mono<Void> saveSelectedChannel(GatewayDiscordClient gateway, DiscordBotType botType, String guildId,
                                           String channelId, DiscordPostType postType) {
        return Mono.fromRunnable(() -> {
            DiscordServer server = discordServerRepository.findByServerIdAndBotType(guildId, botType)
                    .orElseThrow(() -> new IllegalStateException("Server not found: " + guildId));

            // Fetch the channel name
            String channelName = gateway.getGuildById(Snowflake.of(guildId))
                    .flatMapMany(Guild::getChannels)
                    .filter(channel -> channel.getId().asString().equals(channelId))
                    .map(GuildChannel::getName)
                    .blockFirst();

            DiscordActiveChannels activeChannel = new DiscordActiveChannels();
            activeChannel.setDiscordServer(server);
            activeChannel.setChannelId(channelId);
            activeChannel.setChannelName(channelName);
            activeChannel.setPostType(postType);
            activeChannel.setLastPostAt(LocalDateTime.now()); // Set the current time as the last post time

            discordActiveChannelsRepository.save(activeChannel);
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    private Mono<Void> removeSelectedChannel(String channelId, DiscordPostType postType) {
        return Mono.fromRunnable(() -> {
            discordActiveChannelsRepository.findByChannelIdAndPostType(channelId, postType).ifPresent(discordActiveChannelsRepository::delete);
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }
}

package de.gregord.drlleaderboardbackend.services.discord;

import de.gregord.drlleaderboardbackend.entities.DiscordActiveChannels;
import de.gregord.drlleaderboardbackend.entities.DiscordServer;
import de.gregord.drlleaderboardbackend.repositories.DiscordActiveChannelsRepository;
import de.gregord.drlleaderboardbackend.repositories.DiscordServerRepository;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.channel.GuildChannel;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@Service
public class DiscordCommandService {
    private static final Logger LOG = LoggerFactory.getLogger(DiscordCommandService.class);
    private final DiscordInitializationService discordInitializationService;
    private final DiscordServerRepository discordServerRepository;
    private final DiscordActiveChannelsRepository discordActiveChannelsRepository;

    public DiscordCommandService(
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

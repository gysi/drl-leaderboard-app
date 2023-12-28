package de.gregord.drlleaderboardbackend.services;

import de.gregord.drlleaderboardbackend.domain.PlayerImprovement;
import de.gregord.drlleaderboardbackend.entities.DiscordActiveChannels;
import de.gregord.drlleaderboardbackend.entities.DiscordServer;
import de.gregord.drlleaderboardbackend.repositories.DiscordActiveChannelsRepository;
import de.gregord.drlleaderboardbackend.repositories.DiscordServerRepository;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.guild.GuildDeleteEvent;
import discord4j.core.event.domain.interaction.AutoCompleteInteractionEvent;
import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.command.ApplicationCommand;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.SelectMenu;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.*;
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.util.Color;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static de.gregord.drlleaderboardbackend.util.discord.ColorHelper.backgroundColorByPosition;

@Service
public class DiscordService {
    private static final Logger LOG = LoggerFactory.getLogger(DiscordService.class);
    private final String token;
    private final String frontendUrl;
    private DiscordClient client;
    private final DiscordServerRepository discordServerRepository;
    private final DiscordActiveChannelsRepository discordActiveChannelsRepository;
    private GatewayDiscordClient gateway;

    public DiscordService(
            @Value("${app.discord.token}") String token,
            @Value("${app.frontend-url}") String frontendUrl,
            DiscordServerRepository discordServerRepository,
            DiscordActiveChannelsRepository discordActiveChannelsRepository
    ) {
        this.token = token;
        this.discordServerRepository = discordServerRepository;
        this.discordActiveChannelsRepository = discordActiveChannelsRepository;
        this.frontendUrl = frontendUrl;
    }

    @PostConstruct
    public void init() {
        this.client = DiscordClient.create(token);
        this.client.withGateway(this::initializeGateway).subscribe();
    }

    private Mono<Void> initializeGateway(GatewayDiscordClient gateway) {
        this.gateway = gateway;
        return Mono.when(
                registerSlashCommandsOnReady(),
                handleGuildCreateEvents(),
                handleGuildDeleteEvents(),
                handleChatInputInteractionEvents(),
                handleAutoCompleteInteractionEvents()
        );
    }

    private Mono<Void> registerSlashCommandsOnReady() {
        return this.gateway.on(ReadyEvent.class, event -> {
            event.getGuilds().forEach(guild -> {
                registerSlashCommandForGuild(guild.getId(), "drl-leaderboard-posts-activate", "Activate leaderboard posts");
                registerSlashCommandForGuild(guild.getId(), "drl-leaderboard-posts-deactivate", "Deactivate leaderboard posts");
            });
            return Mono.empty();
        }).then();
    }

    private void registerSlashCommandForGuild(Snowflake guildId, String commandName, String description) {
        ApplicationCommandOptionData channelOption = ApplicationCommandOptionData.builder()
                .name("channel")
                .description("Select a channel")
                .type(ApplicationCommandOption.Type.STRING.getValue())
                .required(true)
                .autocomplete(true)
                .build();

        this.gateway.getRestClient().getApplicationService()
                .createGuildApplicationCommand(this.gateway.getRestClient().getApplicationId().block(), guildId.asLong(),
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

    private Mono<Void> handleGuildCreateEvents() {
        return this.gateway.on(GuildCreateEvent.class, event -> {
            Snowflake guildId = event.getGuild().getId();
            String guildName = event.getGuild().getName();
            LOG.info("Received GuildCreateEvent for guild: {} ({})", guildName, guildId.asString());
            // Register commands for the new guild
            registerSlashCommandForGuild(guildId, "drl-leaderboard-posts-activate", "Activate leaderboard posts");
            registerSlashCommandForGuild(guildId, "drl-leaderboard-posts-deactivate", "Deactivate leaderboard posts");

            // Check and create server in database
            return checkAndCreateServer(guildId.asString(), guildName)
                    .doOnSuccess(unused -> LOG.info("Successfully processed GuildCreateEvent for guild: {} ({})", guildName, guildId.asString()))
                    .doOnError(error -> LOG.error("Error processing GuildCreateEvent for guild: {} ({}): {}", guildName, guildId.asString(), error.getMessage()));
        }).then();
    }


    private Mono<Void> checkAndCreateServer(String guildId, String guildName) {
        return Mono.fromCallable(() -> discordServerRepository.findByServerId(guildId))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalDiscordServer -> {
                    if (optionalDiscordServer.isEmpty()) {
                        return createNewServer(guildId, guildName);
                    }
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

    private Mono<Void> handleGuildDeleteEvents() {
        return this.gateway.on(GuildDeleteEvent.class, event -> {
            if (!event.isUnavailable()) {
                String guildId = event.getGuildId().asString();
                return removeServer(guildId);
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

    private Mono<Void> handleChatInputInteractionEvents() {
        return this.gateway.on(ChatInputInteractionEvent.class, event -> {
            String commandName = event.getCommandName();
            String guildId = event.getInteraction().getGuildId().map(Snowflake::asString).orElse("Unknown Guild");
            String channelId = event.getInteraction().getChannelId().asString();

            LOG.info("Received ChatInputInteractionEvent: Command = '{}', Guild ID = '{}', Channel ID = '{}'",
                    commandName, guildId, channelId);

            if ("drl-leaderboard-posts-activate".equals(commandName)) {
                return handleActivateCommand(event);
            } else if ("drl-leaderboard-posts-deactivate".equals(commandName)) {
                return handleDeactivateCommand(event);
            }
            return Mono.empty();
        }).then();
    }

    private Mono<Void> handleAutoCompleteInteractionEvents() {
        return this.gateway.on(ChatInputAutoCompleteEvent.class, event -> {
            if (Arrays.asList("drl-leaderboard-posts-activate", "drl-leaderboard-posts-deactivate").contains(event.getCommandName())
                    && event.getFocusedOption().getName().equals("channel")) {
                String input = event.getFocusedOption().getValue().map(ApplicationCommandInteractionOptionValue::asString).orElse("");
                return provideChannelSuggestions(event, input);
            }
            return Mono.empty();
        }).then();
    }

    private Mono<Void> provideChannelSuggestions(ChatInputAutoCompleteEvent event, String input) {
        Mono<Set<String>> activatedChannelIds = (event.getCommandName().equals("drl-leaderboard-posts-deactivate"))
                ? getActivatedChannelIds(event.getInteraction().getGuildId().orElse(null))
                : Mono.just(Collections.emptySet());

        return activatedChannelIds.flatMap(activatedIds -> event.getInteraction().getGuild().flatMapMany(Guild::getChannels)
                        .filter(channel -> channel.getName().toLowerCase().contains(input.toLowerCase()) &&
                                (activatedIds.isEmpty() || activatedIds.contains(channel.getId().asString())) && channel instanceof TextChannel)
                        .take(25) // Limit to 25 suggestions
                        .map(channel -> ApplicationCommandOptionChoiceData.builder()
                                .name(channel.getName())
                                .value(channel.getId().asString())
                                .build())
                        .collectList()
                        .flatMap(choices -> {
                            List<ApplicationCommandOptionChoiceData> convertedChoices = choices.stream()
                                    .map(choice -> ApplicationCommandOptionChoiceData.builder()
                                            .name(choice.name())
                                            .value(choice.value())
                                            .build())
                                    .collect(Collectors.toList());
                            return event.respondWithSuggestions(convertedChoices);
                        }))
                .then();
    }

    private Mono<Void> handleActivateCommand(ChatInputInteractionEvent event) {
        String guildId = event.getInteraction().getGuildId().orElseThrow().asString();
        String channelId = event.getOption("channel")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);

        if (channelId == null) {
            return event.reply("No channel selected").withEphemeral(true);
        }

        return saveSelectedChannel(guildId, channelId, "LEADERBOARD_POSTS")
                .then(Mono.defer(() -> event.reply("Channel activated for leaderboard posts").withEphemeral(true)))
                .onErrorResume(e -> {
                    LOG.error("Error activating channel: {}", e.getMessage());
                    return event.reply("Failed to activate channel due to an error").withEphemeral(true);
                });
    }


    private Mono<Void> handleDeactivateCommand(ChatInputInteractionEvent event) {
        String channelId = event.getOption("channel")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);

        if (channelId == null) {
            return event.reply("No channel selected").withEphemeral(true);
        }

        return removeSelectedChannel(channelId)
                .then(Mono.defer(() -> event.reply("Channel deactivated for leaderboard posts").withEphemeral(true)))
                .onErrorResume(e -> {
                    LOG.error("Error deactivating channel: {}", e.getMessage());
                    return event.reply("Failed to deactivate channel due to an error").withEphemeral(true);
                });
    }

    private Mono<Set<String>> getActivatedChannelIds(Snowflake guildId) {
        if (guildId == null) {
            return Mono.just(Collections.emptySet());
        }
        return Mono.fromCallable(() -> discordServerRepository.findByServerId(guildId.asString()))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalDiscordServer -> optionalDiscordServer
                        .map(discordServer -> Mono.fromCallable(() -> discordActiveChannelsRepository.findByDiscordServerAndPostType(discordServer, "LEADERBOARD_POSTS"))
                                .subscribeOn(Schedulers.boundedElastic())
                                .flatMapIterable(Function.identity())
                                .map(DiscordActiveChannels::getChannelId)
                                .collect(Collectors.toSet()))
                        .orElseGet(Mono::empty));
    }

    private Mono<Void> saveSelectedChannel(String guildId, String channelId, String postType) {
        return Mono.fromRunnable(() -> {
            DiscordServer server = discordServerRepository.findByServerId(guildId)
                    .orElseThrow(() -> new IllegalStateException("Server not found: " + guildId));

            // Fetch the channel name
            String channelName = this.gateway.getGuildById(Snowflake.of(guildId))
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
        }).onErrorResume(e -> {
            LOG.error("Error saving selected channel: {}", e.getMessage());
            return Mono.empty(); // continue the stream without terminating
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    private Mono<Void> removeSelectedChannel(String channelId) {
        return Mono.fromRunnable(() -> {
            discordActiveChannelsRepository.findByChannelId(channelId).ifPresent(discordActiveChannelsRepository::delete);
        }).onErrorResume(e -> {
            LOG.error("Error removing selected channel: {}", e.getMessage());
            return Mono.empty(); // continue the stream without terminating
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    public void sendMessageToLeaderboardPostsChannels(List<PlayerImprovement> playerImprovements) {
        Optional<LocalDateTime> latestCreatedAtOpt = playerImprovements.stream()
                .filter(imp -> !imp.getForcePost())
                .map(PlayerImprovement::getCreatedAt)
                .max(LocalDateTime::compareTo);

        Mono.fromCallable(() -> discordActiveChannelsRepository.findByPostType("LEADERBOARD_POSTS"))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapIterable(Function.identity())
                .flatMap(channel -> {
                    List<PlayerImprovement> relevantImprovements = playerImprovements.stream()
                            .filter(imp -> imp.getForcePost() || channel.getLastPostAt().isBefore(imp.getCreatedAt()))
                            .collect(Collectors.toList());

                    if (!relevantImprovements.isEmpty()) {
                        latestCreatedAtOpt.ifPresent(latestCreatedAt ->
                                updateLastPostAt(channel, latestCreatedAt));

                        return sendMessageWithEmbedsToChannel(
                                channel.getChannelId(),
                                "### \uD83C\uDFC6 Leaderboard Updates (Top50) \uD83C\uDFC6",
                                createEmbedsForPlayerImprovements(relevantImprovements));
                    } else {
                        // Logging improvements not posted to the channel
                        playerImprovements.stream()
                                .filter(imp -> !imp.getForcePost() && !channel.getLastPostAt().isBefore(imp.getCreatedAt()))
                                .forEach(imp -> LOG.info("Improvement not posted \n {} \n in channel: {} ({}) ", imp, channel.getChannelName(), channel.getChannelId()));
                        return Mono.empty();
                    }
                })
                .subscribe(
                        result -> LOG.info("Message sent successfully to channel"),
                        error -> LOG.error("Error sending message to channel: {}", error.getMessage())
                );
    }

    private List<EmbedCreateSpec> createEmbedsForPlayerImprovements(List<PlayerImprovement> playerImprovements) {
        return playerImprovements.stream()
                .map(imp -> {
                    Color color = backgroundColorByPosition(imp.getCurrentPosition());

                    String formattedCurrentScore = PlayerImprovement.formatScore(imp.getCurrentScore());
                    String scoreImprovementPart = "";
                    String leaderScorePart = "";

                    if (imp.getPreviousPosition() != null) {
                        String scoreDifference = PlayerImprovement.getScoreDifference(imp.getPreviousScore(), imp.getCurrentScore());
                        scoreImprovementPart = String.format("Faster by: %s\n", scoreDifference);
                    }

                    if (imp.getCurrentPosition() == 1) {
                        leaderScorePart = "This time secures the top position as the current track leader!";
                    } else if (imp.getLeaderScore() != null) {
                        String leaderScoreDifference = PlayerImprovement.getScoreDifference(imp.getCurrentScore(), imp.getLeaderScore());
                        String formattedLeaderScore = PlayerImprovement.formatScore(imp.getLeaderScore());
                        leaderScorePart = String.format("Behind the track leader by: %s (%s)", leaderScoreDifference, formattedLeaderScore);
                    }

                    String description = (imp.getPreviousPosition() == null ?
                            String.format("Improved from position >100 to **%d** on\n**%s - %s - %s**\n",
                                    imp.getCurrentPosition(), imp.getTrack().getName(), imp.getTrack().getMapName(),
                                    imp.getTrack().getParentCategory()) :
                            String.format("Improved from position **%d** to **%d** on\n**%s - %s - %s**\n",
                                    imp.getPreviousPosition(), imp.getCurrentPosition(), imp.getTrack().getName(),
                                    imp.getTrack().getMapName(), imp.getTrack().getParentCategory()))
                            + scoreImprovementPart
                            + String.format("New Time: %s\n", formattedCurrentScore)
                            + leaderScorePart;

                    return EmbedCreateSpec.builder()
                            .title(String.format("%s #%d", imp.getPlayerName(), imp.getCurrentPosition()))
                            .description(description)
                            .color(color)
                            .thumbnail(imp.getProfilePicture())
                            .url(String.format("%s/track-lb?trackId=%d", frontendUrl, imp.getTrack().getId()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    private void updateLastPostAt(DiscordActiveChannels channel, LocalDateTime dateTime) {
        channel.setLastPostAt(dateTime);
        discordActiveChannelsRepository.save(channel);
    }

    private Mono<Void> sendMessageWithEmbedsToChannel(String channelId, String message, List<EmbedCreateSpec> embeds) {
        final int chunkSize = 10;
        AtomicInteger counter = new AtomicInteger();

        // Flag to identify the first chunk
        AtomicBoolean isFirstChunk = new AtomicBoolean(true);

        return Flux.fromIterable(embeds.stream()
                        .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / chunkSize))
                        .values())
                .flatMap(embedChunk -> {
                    MessageCreateSpec.Builder messageBuilder = MessageCreateSpec.builder().addAllEmbeds(embedChunk);
                    if (isFirstChunk.getAndSet(false)) { // Check and update the flag
                        messageBuilder.content(message);
                    }
                    return this.gateway.getChannelById(Snowflake.of(channelId))
                            .ofType(TextChannel.class)
                            .flatMap(channel -> channel.createMessage(messageBuilder.build()));
                }).then();
    }
}

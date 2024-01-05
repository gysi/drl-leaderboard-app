package de.gregord.drlleaderboardbackend.services.discord;

import de.gregord.drlleaderboardbackend.domain.PlayerImprovement;
import de.gregord.drlleaderboardbackend.entities.DiscordActiveChannels;
import de.gregord.drlleaderboardbackend.repositories.DiscordActiveChannelsRepository;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static de.gregord.drlleaderboardbackend.util.discord.ColorHelper.backgroundColorByPosition;

@Service
public class DiscordMessageService {
    private static final Logger LOG = LoggerFactory.getLogger(DiscordMessageService.class);
    private final DiscordInitializationService discordInitializationService;
    private final DiscordActiveChannelsRepository discordActiveChannelsRepository;
    private final String frontendUrl;

    public DiscordMessageService(
            DiscordInitializationService discordInitializationService,
            DiscordActiveChannelsRepository discordActiveChannelsRepository,
            @Value("${app.frontend-url}") String frontendUrl
    ) {
        this.discordInitializationService = discordInitializationService;
        this.discordActiveChannelsRepository = discordActiveChannelsRepository;
        this.frontendUrl = frontendUrl;
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
        CompletableFuture<GatewayDiscordClient> gatewayFuture = discordInitializationService.getGateway();
        return Mono.fromFuture(gatewayFuture)
                .flatMapMany(gateway -> Flux.fromIterable(embeds.stream()
                                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / chunkSize))
                                .values())
                                .flatMap(embedChunk -> {
                                    MessageCreateSpec.Builder messageBuilder = MessageCreateSpec.builder().addAllEmbeds(embedChunk);
                                    if (isFirstChunk.getAndSet(false)) {
                                        messageBuilder.content(message);
                                    }
                                    return gateway.getChannelById(Snowflake.of(channelId))
                                            .ofType(TextChannel.class)
                                            .flatMap(channel -> channel.createMessage(messageBuilder.build()));
                                })
                ).then();
    }
}

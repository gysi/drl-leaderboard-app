package de.gregord.drlleaderboardbackend.services.discord;

import de.gregord.drlleaderboardbackend.domain.PlayerImprovement;
import de.gregord.drlleaderboardbackend.entities.DiscordActiveChannels;
import de.gregord.drlleaderboardbackend.entities.DiscordServerSetting;
import de.gregord.drlleaderboardbackend.entities.Tournament;
import de.gregord.drlleaderboardbackend.entities.tournament.TournamentRanking;
import de.gregord.drlleaderboardbackend.entities.tournament.TournamentRound;
import de.gregord.drlleaderboardbackend.repositories.DiscordActiveChannelsRepository;
import de.gregord.drlleaderboardbackend.repositories.DiscordServerSettingsRepository;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.EmbedCreateFields;
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
import java.util.*;
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
    private final DiscordServerSettingsRepository discordServerSettingRepository;
    private final String frontendUrl;

    public DiscordMessageService(
            DiscordInitializationService discordInitializationService,
            DiscordActiveChannelsRepository discordActiveChannelsRepository,
            DiscordServerSettingsRepository discordServerSettingRepository,
            @Value("${app.frontend-url}") String frontendUrl
    ) {
        this.discordInitializationService = discordInitializationService;
        this.discordActiveChannelsRepository = discordActiveChannelsRepository;
        this.discordServerSettingRepository = discordServerSettingRepository;
        this.frontendUrl = frontendUrl;
    }

    public void sendMessageToLeaderboardPostsChannels(List<PlayerImprovement> playerImprovements) {
        if (playerImprovements == null || playerImprovements.isEmpty()) {
            return;
        }
        Optional<LocalDateTime> latestCreatedAtOpt = playerImprovements.stream()
                .filter(imp -> !imp.getForcePost())
                .map(PlayerImprovement::getCreatedAt)
                .max(LocalDateTime::compareTo);

        Mono.fromCallable(() -> discordActiveChannelsRepository.findByPostType(DiscordPostType.LEADERBOARD_POSTS))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapIterable(Function.identity())
                .flatMap(channel -> {
                    // TODO should now work without the outcommented code, remove it in the future
//                    List<PlayerImprovement> relevantImprovements = playerImprovements.stream()
//                            .filter(imp -> imp.getForcePost() || channel.getLastPostAt().isBefore(imp.getCreatedAt()))
//                            .collect(Collectors.toList());
                    if(playerImprovements.isEmpty()){
//                        playerImprovements.stream()
//                                .filter(imp -> !imp.getForcePost() && !channel.getLastPostAt().isBefore(imp.getCreatedAt()))
//                                .forEach(imp -> LOG.info("Improvement not posted \n {} \n in channel: {} ({}) ", imp,
//                                        channel.getChannelName(), channel.getChannelId()));
                        return Mono.empty();
                    }
                    latestCreatedAtOpt.ifPresent(latestCreatedAt ->
                            updateLastPostAt(channel, latestCreatedAt));
                    return sendMessageWithEmbedsToChannel(
                            discordInitializationService.getLeaderboardGateway(),
                            channel.getChannelId(),
                            "### \uD83C\uDFC6 Leaderboard Updates (Top50) \uD83C\uDFC6",
                            createEmbedsForPlayerImprovements(playerImprovements));
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
                            .url(String.format("%s/track-leaderboard?trackId=%d", frontendUrl, imp.getTrack().getId()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    private void updateLastPostAt(DiscordActiveChannels channel, LocalDateTime dateTime) {
        channel.setLastPostAt(dateTime);
        discordActiveChannelsRepository.save(channel);
    }

    private Mono<Void> sendMessageWithEmbedsToChannel(CompletableFuture<GatewayDiscordClient> gatewayFuture,
                                                      String channelId, String message, List<EmbedCreateSpec> embeds) {
        final int chunkSize = 10;
        AtomicInteger counter = new AtomicInteger();

        // Flag to identify the first chunk
        AtomicBoolean isFirstChunk = new AtomicBoolean(true);
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

    public void sendMessageToTournamentReminderChannel(Tournament tournament, Boolean justStarted, Boolean isTest) {
        sendMessageToTournamentReminderChannelFlux(tournament, justStarted, isTest).subscribe(
                result -> LOG.info("Message sent successfully to channel"),
                error -> LOG.error("Error sending message to channel: {}", error.getMessage())
        );
    }

    public Flux<Void> sendMessageToTournamentReminderChannelFlux(Tournament tournament, Boolean justStarted, Boolean isTest) {
        return Mono.fromCallable(() -> discordActiveChannelsRepository.findByPostType(DiscordPostType.TOURNAMENT_REMINDER))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable)
                .flatMap(channel -> {
                    // Reactive handling of the repository call
                    return Mono.fromCallable(() ->
                                    discordServerSettingRepository.findByServerIdAndSetting(
                                            channel.getDiscordServer().getId(), DiscordServerSetting.Settings.TAG_ROLE)
                            )
                            .subscribeOn(Schedulers.boundedElastic())
                            .flatMap(roleTaggingSetting -> {
                                // Calculate the adjusted registration end date
                                LocalDateTime adjustedRegistrationEndDate = tournament.getRegistrationEndAt().minusMinutes(30);
                                // Compare lastPostAt with the adjusted registration end date
                                if (justStarted
                                        || (channel.getLastPostAt() != null && channel.getLastPostAt().isBefore(adjustedRegistrationEndDate))
                                        || isTest) {
                                    if(!isTest) {
                                        updateLastPostAt(channel, LocalDateTime.now());
                                    }
                                    String roleTag = roleTaggingSetting
                                            .map(DiscordServerSetting::getValue)
                                            .map(value -> {
                                                if(value.contains("@")){
                                                    return value;
                                                } else {
                                                    return "<@&" + value + ">";
                                                }
                                            })
                                            .orElse("");

                                    return sendMessageWithEmbedsToChannel(
                                            discordInitializationService.getTournamentGateway(),
                                            channel.getChannelId(),
                                            roleTag,
                                            justStarted ? createEmbedsForTournamentStart(tournament, roleTag) :
                                                    createEmbedsForTournamentReminder(tournament, roleTag)
                                    );
                                } else {
                                    return Mono.empty(); // Skip sending message if condition not met
                                }
                            });
                });
    }

    private List<EmbedCreateSpec> createEmbedsForTournamentReminder(Tournament tournament, String roleTag) {
        Optional<TournamentRound> qualifyingRound = tournament.getRounds().stream().min(Comparator.comparing(TournamentRound::getNOrder));
        List<TournamentRound.Player> players = qualifyingRound.flatMap(rounds -> rounds.getMatches().stream().findFirst())
                .map(TournamentRound.Match::getPlayers).orElse(Collections.emptyList());
        List<EmbedCreateFields.Field> participantsEmbedFields = createEmbedFieldsForParticipants(
                players.stream().map(TournamentRound.Player::getProfileName).collect(Collectors.toList()));
        EmbedCreateSpec.Builder builder = EmbedCreateSpec.builder()
                .author(EmbedCreateFields.Author.of("\uD83C\uDFC6 Tournament Reminder \uD83C\uDFC6", null, null))
                .title(String.format("%s\nis about to start in 30min!", tournament.getTitle()))
                .url(String.format("%s/tournaments", frontendUrl))
                .addField(EmbedCreateFields.Field.of("", "Participants:", false))
                .addFields(participantsEmbedFields.toArray(EmbedCreateFields.Field[]::new))
                .addField(EmbedCreateFields.Field.of("Go start the SIM and get warmed up!","", false))
                .footer("HAVE FUN!", null)
                .color(Color.of(67, 214, 214));
        if (tournament.getImageUrl() != null) {
            builder.thumbnail(tournament.getImageUrl());
        }
        return List.of(builder.build());
    }

    private List<EmbedCreateSpec> createEmbedsForTournamentStart(Tournament tournament, String roleTag) {
        Optional<TournamentRound> qualifyingRound = tournament.getRounds().stream().min(Comparator.comparing(TournamentRound::getNOrder));
        List<TournamentRound.Player> players = qualifyingRound.flatMap(rounds -> rounds.getMatches().stream().findFirst())
                .map(TournamentRound.Match::getPlayers).orElse(Collections.emptyList());
        List<EmbedCreateFields.Field> participantsEmbedFields = createEmbedFieldsForParticipants(
                players.stream().map(TournamentRound.Player::getProfileName).collect(Collectors.toList()));
        EmbedCreateSpec.Builder builder = EmbedCreateSpec.builder()
                .author(EmbedCreateFields.Author.of("\uD83C\uDFC6 Tournament Started \uD83C\uDFC6", null, null))
                .title(String.format("%s", tournament.getTitle()))
                .url(String.format("%s/tournaments", frontendUrl))
                .addField(EmbedCreateFields.Field.of(String.format("Track: %s", tournament.getCustomMapTitle()), "", false))
                .addField(EmbedCreateFields.Field.of("", "Participants:", false))
                .addFields(participantsEmbedFields.toArray(EmbedCreateFields.Field[]::new))
                .addField(EmbedCreateFields.Field.of("You have 20min to qualify!", "", false))
                .footer("HAVE FUN!", null)
                .color(Color.of(0, 255, 0));
        if (tournament.getImageUrl() != null) {
            builder.thumbnail(tournament.getImageUrl());
        }
        return List.of(builder.build());
    }

    public void sendMessageToTournamentResultChannel(Tournament tournament, Boolean isTest) {
        sendMessageToTournamentResultChannelFlux(tournament, isTest)
                .subscribe(
                        result -> LOG.info("Message sent successfully to channel"),
                        error -> LOG.error("Error sending message to channel: {}", error.getMessage())
                );
    }

    public Flux<Void> sendMessageToTournamentResultChannelFlux(Tournament tournament, Boolean isTest) {
        return Mono.fromCallable(() -> discordActiveChannelsRepository.findByPostType(DiscordPostType.TOURNAMENT_RESULTS))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapIterable(Function.identity())
                .flatMap(channel -> {
                    if(!isTest) {
                        updateLastPostAt(channel, LocalDateTime.now());
                    }
                    return sendMessageWithEmbedsToChannel(
                            discordInitializationService.getTournamentGateway(),
                            channel.getChannelId(),
                            "",
                            createEmbedsForTournamentResult(tournament));
                });
    }

    private List<EmbedCreateSpec> createEmbedsForTournamentResult(Tournament tournament) {
        String winner = "\uD83C\uDFC6";
        String goldenheat = "\uD83C\uDF1F";
        List<TournamentRanking> rankings = tournament.getRankings();
        AtomicInteger counter = new AtomicInteger(1);
        List<String> players = rankings.stream().map(playerRanking -> {
            String playerName = playerRanking.getProfileName();
            String score = PlayerImprovement.formatScore(playerRanking.getScore());
            if (counter.get() == 1) {
                playerName = String.format("%d. %s %s %s  (%s)", counter.get(), playerName, winner, goldenheat, score);
            } else {
                playerName = playerRanking.getGoldenPos() != null && playerRanking.getGoldenPos() > 0 ?
                        String.format("%d. %s %s  (%s)", counter.get(), playerName, goldenheat, score) :
                        String.format("%d. %s ", counter.get(), playerName);
            }
            counter.getAndIncrement();
            return playerName;
        }).limit(10).collect(Collectors.toList());
        List<EmbedCreateFields.Field> participantsEmbedFields = createEmbedFieldsForParticipants(players);
        EmbedCreateSpec.Builder builder = EmbedCreateSpec.builder()
                .author(EmbedCreateFields.Author.of("\uD83C\uDFC6 Tournament Finished \uD83C\uDFC6", null, null))
                .title(String.format("%s", tournament.getTitle()))
                .url(String.format("%s/tournaments", frontendUrl))
                .addField(EmbedCreateFields.Field.of(String.format("Track: %s", tournament.getCustomMapTitle()), "", false))
                .addField(EmbedCreateFields.Field.of("", "Results:", false))
                .addFields(participantsEmbedFields.toArray(new EmbedCreateFields.Field[0]))
                .addField(EmbedCreateFields.Field.of("GGs to everyone!", "", false))
                .footer("Seeya at the next one..", null)
                .color(Color.of(255, 255, 0));
        if (tournament.getImageUrl() != null) {
            builder.thumbnail(tournament.getImageUrl());
        }
        return List.of(builder.build());
    }

    private List<EmbedCreateFields.Field> createEmbedFieldsForParticipants(List<String> players) {
        List<EmbedCreateFields.Field> fields = new ArrayList<>();
        String allPlayers = String.join("\n", players);

        // Check if the total length exceeds 1021 characters
        if (allPlayers.length() > 1021) {
            // Truncate the list of players to fit within the character limit
            while (allPlayers.length() > 1021 && !players.isEmpty()) {
                players.remove(players.size() - 1); // Remove players from the end
                allPlayers = String.join("\n", players);
            }
            allPlayers += "..."; // Add ellipsis to indicate truncation
        }

        // Now split the list into two fields if there are more than 5 players
        if (players.size() > 5) {
            int splitPoint = players.size() / 2;
            if (players.size() % 2 != 0) {
                splitPoint++;
            }
            List<String> participants1 = players.subList(0, splitPoint);
            List<String> participants2 = players.subList(splitPoint, players.size());

            fields.add(EmbedCreateFields.Field.of("", String.join("\n", participants1), true));
            fields.add(EmbedCreateFields.Field.of("", String.join("\n", participants2), true));
        } else {
            fields.add(EmbedCreateFields.Field.of("", allPlayers, true));
        }
        return fields;
    }

}

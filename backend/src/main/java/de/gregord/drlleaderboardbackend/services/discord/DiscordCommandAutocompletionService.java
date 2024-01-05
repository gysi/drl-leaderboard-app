package de.gregord.drlleaderboardbackend.services.discord;

import de.gregord.drlleaderboardbackend.entities.DiscordActiveChannels;
import de.gregord.drlleaderboardbackend.repositories.DiscordActiveChannelsRepository;
import de.gregord.drlleaderboardbackend.repositories.DiscordServerRepository;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DiscordCommandAutocompletionService {

    private final DiscordInitializationService discordInitializationService;
    private final DiscordServerRepository discordServerRepository;
    private final DiscordActiveChannelsRepository discordActiveChannelsRepository;

    public DiscordCommandAutocompletionService(
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
            handleAutoCompleteInteractionEvents(gateway).subscribe();
        });
    }

    private Mono<Void> handleAutoCompleteInteractionEvents(GatewayDiscordClient gateway) {
        return gateway.on(ChatInputAutoCompleteEvent.class, event -> {
            if (Arrays.asList(
                    "drl-leaderboard-posts-activate",
                    "drl-leaderboard-posts-deactivate"
            ).contains(event.getCommandName())
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
}

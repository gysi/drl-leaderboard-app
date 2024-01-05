package de.gregord.drlleaderboardbackend.services.discord;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class DiscordInitializationService {
    private final String token;
    private final CompletableFuture<GatewayDiscordClient> gatewayFuture = new CompletableFuture<>();

    public DiscordInitializationService(
            @Value("${app.discord.token}") String token
    ) {
        this.token = token;
    }

    @PostConstruct
    public void init() {
        DiscordClient client = DiscordClient.create(token);
        client.login().subscribe(this.gatewayFuture::complete, this.gatewayFuture::completeExceptionally);
    }

    public CompletableFuture<GatewayDiscordClient> getGateway() {
        return gatewayFuture;
    }
}

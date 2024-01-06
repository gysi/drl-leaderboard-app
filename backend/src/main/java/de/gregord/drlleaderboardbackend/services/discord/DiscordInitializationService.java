package de.gregord.drlleaderboardbackend.services.discord;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class DiscordInitializationService {
    private final String leaderboardToken;
    private final String tournamentToken;
    private final CompletableFuture<GatewayDiscordClient> leaderboardGatewayFuture = new CompletableFuture<>();
    private final CompletableFuture<GatewayDiscordClient> tournamentGatewayFuture = new CompletableFuture<>();

    public DiscordInitializationService(
            @Value("${app.discord.leaderboard.token}") String leaderboardToken,
            @Value("${app.discord.tournament.token}") String tournamentToken
    ) {
        this.leaderboardToken = leaderboardToken;
        this.tournamentToken = tournamentToken;
    }

    @PostConstruct
    public void init() {
        DiscordClient.create(leaderboardToken).login().subscribe(this.leaderboardGatewayFuture::complete, this.leaderboardGatewayFuture::completeExceptionally);
        DiscordClient.create(tournamentToken).login().subscribe(this.tournamentGatewayFuture::complete, this.tournamentGatewayFuture::completeExceptionally);
    }

    public CompletableFuture<GatewayDiscordClient> getLeaderboardGateway() {
        return leaderboardGatewayFuture;
    }

    public CompletableFuture<GatewayDiscordClient> getTournamentGateway() {
        return tournamentGatewayFuture;
    }
}

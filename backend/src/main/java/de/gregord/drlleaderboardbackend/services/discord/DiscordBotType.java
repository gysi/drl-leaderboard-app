package de.gregord.drlleaderboardbackend.services.discord;

import java.util.List;

public enum DiscordBotType {
    LEADERBOARD(List.of(
            DiscordCommand.LEADERBOARD_POSTS_ACTIVATE,
            DiscordCommand.LEADERBOARD_POSTS_DEACTIVATE
    )),
    TOURNAMENT(List.of(
            DiscordCommand.TOURNAMENT_REMINDER_ACTIVATE,
            DiscordCommand.TOURNAMENT_REMINDER_DEACTIVATE,
            DiscordCommand.TOURNAMENT_REMINDER_TAG_ROLE,
            DiscordCommand.TOURNAMENT_REMINDER_TAG_ROLE_REMOVE,
            DiscordCommand.TOURNAMENT_REMINDER_TEST,
            DiscordCommand.TOURNAMENT_RESULTS_ACTIVATE,
            DiscordCommand.TOURNAMENT_RESULTS_DEACTIVATE,
            DiscordCommand.TOURNAMENT_RESULTS_TEST
    ));

    private final List<DiscordCommand> commands;

    DiscordBotType(List<DiscordCommand> commandPrefixes) {
        this.commands = commandPrefixes;
    }

    public List<DiscordCommand> getCommands() {
        return this.commands;
    }
}

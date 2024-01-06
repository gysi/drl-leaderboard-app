package de.gregord.drlleaderboardbackend.services.discord;

public enum DiscordCommand {
    LEADERBOARD_POSTS_ACTIVATE(
            "leaderboard-posts-activate",
            "Activates leaderboard posts for a channel.",
            DiscordPostType.LEADERBOARD_POSTS,
            true,
            false),
    LEADERBOARD_POSTS_DEACTIVATE(
            "leaderboard-posts-deactivate",
            "Deactivates leaderboard posts for a channel.",
            DiscordPostType.LEADERBOARD_POSTS,
            false,
            true),
    TOURNAMENT_REMINDER_ACTIVATE(
            "tourney-reminder-activate",
            "Activates tournament reminder posts for a channel.",
            DiscordPostType.TOURNAMENT_REMINDER,
            true,
            false),
    TOURNAMENT_REMINDER_DEACTIVATE(
            "tourney-reminder-deactivate",
            "Deactivates tournament reminder posts for a channel.",
            DiscordPostType.TOURNAMENT_REMINDER,
            false,
            true),
    TOURNAMENT_RESULTS_ACTIVATE(
            "tourney-results-activate",
            "Activates tournament result posts for a channel.",
            DiscordPostType.TOURNAMENT_RESULTS,
            true,
            false),
    TOURNAMENT_RESULTS_DEACTIVATE(
            "tourney-results-deactivate",
            "Deactivates tournament result posts for a channel.",
            DiscordPostType.TOURNAMENT_RESULTS,
            false,
            true);

    private final String commandName;
    private final String description;
    private final DiscordPostType postType;
    private final Boolean activatesChannel;
    private final Boolean deactivatesChannel;

    DiscordCommand(String commandName, String description, DiscordPostType postType,
                           Boolean activatesChannel, Boolean deactivatesChannel) {
        this.commandName = commandName;
        this.description = description;
        this.postType = postType;
        this.activatesChannel = activatesChannel;
        this.deactivatesChannel = deactivatesChannel;
    }

    public String getCommandName() {
        return this.commandName;
    }

    public String getDescription() {
        return this.description;
    }

    public DiscordPostType getPostType() {
        return this.postType;
    }

    public Boolean activatesChannel() {
        return this.activatesChannel;
    }

    public Boolean deactivatesChannel() {
        return this.deactivatesChannel;
    }

    public static DiscordCommand fromCommandName(String commandName) {
        for (DiscordCommand command : DiscordCommand.values()) {
            if (command.getCommandName().equals(commandName)) {
                return command;
            }
        }
        return null;
    }
}

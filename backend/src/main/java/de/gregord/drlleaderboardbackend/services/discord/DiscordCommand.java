package de.gregord.drlleaderboardbackend.services.discord;

import de.gregord.drlleaderboardbackend.entities.DiscordServerSetting;

public enum DiscordCommand {
    LEADERBOARD_POSTS_ACTIVATE(
            "leaderboard-posts-activate",
            "Activates leaderboard posts for a channel.",
            DiscordPostType.LEADERBOARD_POSTS,
            true,
            false,
            null,
            false,
            false,
            false),
    LEADERBOARD_POSTS_DEACTIVATE(
            "leaderboard-posts-deactivate",
            "Deactivates leaderboard posts for a channel.",
            DiscordPostType.LEADERBOARD_POSTS,
            false,
            true,
            null,
            false,
            false,
            false),
    TOURNAMENT_REMINDER_ACTIVATE(
            "tourney-reminder-activate",
            "Activates tournament reminder posts for a channel.",
            DiscordPostType.TOURNAMENT_REMINDER,
            true,
            false,
            null,
            false,
            false,
            false),
    TOURNAMENT_REMINDER_DEACTIVATE(
            "tourney-reminder-deactivate",
            "Deactivates tournament reminder posts for a channel.",
            DiscordPostType.TOURNAMENT_REMINDER,
            false,
            true,
            null,
            false,
            false,
            false),
    TOURNAMENT_REMINDER_TAG_ROLE(
            "tourney-reminder-tag-role",
            "Tags a certain role when the tournament is about to start",
            DiscordPostType.TOURNAMENT_REMINDER,
            false,
            false,
            DiscordServerSetting.Settings.TAG_ROLE,
            true,
            false,
            false),
    TOURNAMENT_REMINDER_TAG_ROLE_REMOVE(
            "tourney-reminder-tag-remove",
            "Removes roles tagging again",
            DiscordPostType.TOURNAMENT_REMINDER,
            false,
            false,
            DiscordServerSetting.Settings.TAG_ROLE,
            false,
            true,
            false),
    TOURNAMENT_RESULTS_ACTIVATE(
            "tourney-results-activate",
            "Activates tournament result posts for a channel.",
            DiscordPostType.TOURNAMENT_RESULTS,
            true,
            false,
            null,
            false,
            false,
            false),
    TOURNAMENT_RESULTS_DEACTIVATE(
            "tourney-results-deactivate",
            "Deactivates tournament result posts for a channel.",
            DiscordPostType.TOURNAMENT_RESULTS,
            false,
            true,
            null,
            false,
            false,
            false),
    TOURNAMENT_REMINDER_TEST(
            "tourney-reminder-test",
            "Sends test messages for tournament reminder posts",
            DiscordPostType.TOURNAMENT_REMINDER,
            false,
            false,
            null,
            false,
            false,
            true),
    TOURNAMENT_RESULTS_TEST(
            "tourney-results-test",
            "Sends test messages for tournament result posts",
            DiscordPostType.TOURNAMENT_RESULTS,
            false,
            false,
            null,
            false,
            false,
            true);

    private final String commandName;
    private final String description;
    private final DiscordPostType postType;
    private final Boolean activatesChannel;
    private final Boolean deactivatesChannel;
    private final DiscordServerSetting.Settings setting;
    private final Boolean setsSettingRegardingRoles;
    private final Boolean removesSetting;
    private final Boolean isTestCommand;

    DiscordCommand(String commandName, String description, DiscordPostType postType,
                   Boolean activatesChannel, Boolean deactivatesChannel,
                   DiscordServerSetting.Settings setting,
                   Boolean setsSettingRegardingRoles, Boolean removesSetting,
                   Boolean isTestCommand) {
        this.commandName = commandName;
        this.description = description;
        this.postType = postType;
        this.activatesChannel = activatesChannel;
        this.deactivatesChannel = deactivatesChannel;
        this.setting = setting;
        this.setsSettingRegardingRoles = setsSettingRegardingRoles;
        this.removesSetting = removesSetting;
        this.isTestCommand = isTestCommand;
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

    public Boolean setsSettingRegardingRoles() {
        return this.setsSettingRegardingRoles;
    }

    public Boolean removesSetting() {
        return this.removesSetting;
    }

    public Boolean isTestCommand() {
        return this.isTestCommand;
    }

    public DiscordServerSetting.Settings getSetting() {
        return this.setting;
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

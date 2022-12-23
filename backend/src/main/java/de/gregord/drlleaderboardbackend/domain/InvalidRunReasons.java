package de.gregord.drlleaderboardbackend.domain;

public enum InvalidRunReasons {
    BETTER_ENTRY_WITH_SAME_NAME,
    BETTER_ENTRY_WITH_KNOWN_DOUBLE_ACCOUNT,
    IMPOSSIBLE_TOP_SPEED,
    NO_REPLAY,
}

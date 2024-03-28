package de.gregord.drlleaderboardbackend.entities;

public enum MapDifficulty {
    BASIC, // 0
    EASY, // 1
    MEDIUM, // 2
    HARD; // 3

    public static int getDRLIdForDifficulty(MapDifficulty mapDifficulty){
        return mapDifficulty.ordinal();
    }
}

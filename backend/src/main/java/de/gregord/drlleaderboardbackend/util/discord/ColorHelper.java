package de.gregord.drlleaderboardbackend.util.discord;

import discord4j.rest.util.Color;

public class ColorHelper {
    public static Color backgroundColorByPosition(Long position) {
        if (position > 75) {
            return Color.of(75, 75, 75); // #4B4B4B
        } else if (position > 50) {
            return Color.of(35, 73, 24); // #234918
        } else if (position > 25) {
            return Color.of(50, 103, 34); // #326722
        } else if (position > 10) {
            return Color.of(64, 131, 45); // #40832d
        } else if (position > 3) {
            return Color.of(89, 180, 61); // #59b43d
        } else if (position > 2) {
            return Color.of(187, 107, 33); // rgb(187,107,33) for 3
        } else if (position > 1) {
            return Color.of(138, 135, 141); // rgb(138,135,141) for 2
        } else {
            return Color.of(180, 135, 22); // rgba(180,135,22) for 1
        }
    }
}

package de.gregord.drlleaderboardbackend.domain;

import de.gregord.drlleaderboardbackend.entities.TrackMinimal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class PlayerImprovement {
    private String playerName;
    private Long previousPosition;
    private Long currentPosition;
    private Long previousScore; // time in ms
    private Long currentScore; // time in ms
    private LocalDateTime createdAt;
    private TrackMinimal track;
    private String profilePicture;
    private Boolean forcePost = false;
    private Long leaderScore = null;

    public static String getScoreDifference(Long previousScore, Long currentScore) {
        if (previousScore == null || currentScore == null) {
            return "N/A"; // or any other default representation
        }

        long difference = Math.abs(previousScore - currentScore);

        if (difference == 0) {
            return "0ms"; // No difference
        }

        long minutes = difference / 60000;
        long seconds = (difference % 60000) / 1000;
        long milliseconds = difference % 1000;

        StringBuilder result = new StringBuilder();
        if (minutes > 0) {
            result.append(minutes).append("m ");
        }
        if (seconds > 0 || minutes > 0) {
            result.append(seconds).append("s ");
        }
        result.append(milliseconds).append("ms");

        return result.toString();
    }

    public static String formatScore(Long score) {
        if (score == null) {
            return "N/A"; // or any other default representation
        }

        long milliseconds = Math.abs(score);

        long hours = milliseconds / 3600000;
        long minutes = (milliseconds % 3600000) / 60000;
        long seconds = (milliseconds % 60000) / 1000;
        long millis = milliseconds % 1000;

        String formattedHours = (hours > 0) ? "%d:".formatted(hours) : "";
        String formattedMinutes = (minutes > 0 || hours > 0) ? "%02d:".formatted(minutes) : "";
        String formattedSeconds = (seconds > 0 || minutes > 0 || hours > 0) ? (seconds < 10 && minutes == 0 && hours == 0 ? "%d" : "%02d").formatted(seconds) : "0";
        String formattedMillis = ".%03d".formatted(millis);

        return formattedHours + formattedMinutes + formattedSeconds + formattedMillis;
    }

//    public static void main(String[] args) {
//        PlayerImprovement playerImprovement = new PlayerImprovement();
//        playerImprovement.setPreviousScore(123456L);
//        playerImprovement.setCurrentScore(123456L);
//        System.out.println(playerImprovement.getScoreDifference());
//        playerImprovement.setCurrentScore(123456L + 60000L);
//        System.out.println(playerImprovement.getScoreDifference());
//        playerImprovement.setCurrentScore(123456L + 60000L + 42000L);
//        System.out.println(playerImprovement.getScoreDifference());
//        playerImprovement.setCurrentScore(123456L + 60000L + 42000L + 300L);
//        System.out.println(playerImprovement.getScoreDifference());
//
//        // test format score method with different inputs
//        System.out.println(formatScore(123456L));
//        System.out.println(formatScore(123456L + 60000L));
//        System.out.println(formatScore(123456L + 60000L + 42000L));
//        System.out.println(formatScore(123456L + 60000L + 42000L + 300L));
//        // no minutes, just seconds and milliseconds
//        System.out.println(formatScore(42000L + 300L));
//        System.out.println(formatScore(9000L + 300L));
//        System.out.println(formatScore(9000L));
//        // no minutes, just milliseconds
//        System.out.println(formatScore(300L));
//    }
}

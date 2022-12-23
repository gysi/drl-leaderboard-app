package de.gregord.drlleaderboardbackend.domain;

public class PointsCalculation {
    public static Double calculatePointsByPosition(Double position) {
        Double points = 0.;
        points = 101 - position;
        if (points < 0) {
            return 0.;
        }
        if (position <= 75) {
            points += 5;
        }
        if (position <= 50) {
            points += 5;
        }
        if (position <= 25) {
            points += 5;
        }
        if (position <= 10) {
            points += 5;
        }
        if (position <= 5) {
            points += 2;
        }
        if (position <= 3) {
            points += 2;
        }
        if (position <= 1) {
            points += 2;
        }
        points = Math.pow(points, 1.1);
        return points;
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 100; i++) {
            System.out.println(
                    i + " " + calculatePointsByPosition((double) i)
                            + " (" + (calculatePointsByPosition((double) i) - calculatePointsByPosition((double) (i + 1))) + ")"
            );
        }
    }
}

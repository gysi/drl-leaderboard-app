package de.gregord.drlleaderboardbackend.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PointsCalculation {
    public static final Logger LOG = LoggerFactory.getLogger(PointsCalculation.class);
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

    public static Double calculatePointsByPositionV2(Long firstPositionMS, Long positionMS) {
        if(positionMS < firstPositionMS){
            positionMS = firstPositionMS;
        }
        Double result = Math.min(100_000, 670_000_000. / Math.pow(15., (Math.log10((positionMS - firstPositionMS) + 400.) / 0.8)));
        if(result > 100_000 || result < 0) {
            LOG.error("WTF? positionMS:{} firstPositionMS:{} result:{}", positionMS, firstPositionMS, result);
        }
        return result;
    }

    public static Double calculatePointsByPositionV3(Long position) {
//        Double points = 1561.5 - 761.5 * Math.log10(position + 9);
        //Round(1046.1  - 515.88 * log(position + 2, 10))
        Double points = 1046.1 - 515.88 * Math.log10(position + 2);
        if(position == 1L){
            points = 800.;
        }
        if(position == 100L){
            points = 10.;
        }
        if (position > 100) {
            return 0.;
        }
        if (position <= 75) {
            points += 20;
        }
        if (position <= 50) {
            points += 20;
        }
        if (position <= 25) {
            points += 20;
        }
        if (position <= 10) {
            points += 40;
        }
        if (position <= 5) {
            points += 20;
        }
        if (position <= 4) {
            points += 5;
        }
        if (position <= 3) {
            points += 9;
        }
        if (position <= 2) {
            points += 30;
        }
        if (position <= 1) {
            points += 36;
        }
        return points;
    }

    private static double customLog(double base, double logNumber) {
        return Math.log(logNumber) / Math.log(base);
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 100; i++) {
            System.out.println(
                    i + " " + calculatePointsByPosition((double) i)
                            + " (" + (calculatePointsByPosition((double) i) - calculatePointsByPosition((double) (i + 1))) + ")"
            );
        }

        // some samples for calculatePointsByPositionV2 using for the first parameter always 0 and for the 2nd the values:
        // 0, 10, 25, 50, 100, 200, 500, 1000, 5000, 10000, 15000, 20000
        long[] values = {0, 10, 25, 50, 100, 200, 500, 1000, 5000, 10000, 15000, 20000};
        for(long value : values){
            System.out.println("Position 0, Value: "+value+" Points: "+calculatePointsByPositionV2(0L, value));
        }

        long[] values2 = {1, 2, 3, 4, 5, 10, 25, 50, 75, 100};
        for(long value : values2){
            System.out.println("Position 0, Value: "+value+" Points: "+Math.round(calculatePointsByPositionV3(value)));
        }
    }
}

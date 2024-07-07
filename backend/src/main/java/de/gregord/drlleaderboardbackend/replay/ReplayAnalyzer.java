package de.gregord.drlleaderboardbackend.replay;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.gregord.drlleaderboardbackend.entities.LeaderboardEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.*;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

public class ReplayAnalyzer {
    public static final Logger LOG = LoggerFactory.getLogger(ReplayAnalyzer.class);

    public static class EmptyReplayException extends Exception {}

    public static class MarkerData {
        public List<Float> time = new ArrayList<>();
        public List<Float> dronePosX = new ArrayList<>();
        public List<Float> dronePosY = new ArrayList<>();
        public List<Float> dronePosZ = new ArrayList<>();
        public List<Float> droneVX = new ArrayList<>();
        public List<Float> droneVY = new ArrayList<>();
        public List<Float> droneVZ = new ArrayList<>();
        public List<Float> inputT = new ArrayList<>();
        public List<Float> droneVelocity = new ArrayList<>();
        public List<Double> directionChanges = new ArrayList<>();
    }

    public static void main(String[] args) throws DataFormatException, IOException, EmptyReplayException {
//        String replayUrl = "https://drl-game-api.s3.amazonaws.com/replays/5eb20172df3a8941060eda43/out-of-service.mt-4f8.1717719508319.race";
        String replayUrl = "https://drl-game-api.s3.amazonaws.com/replays/65f9e0490e121e3e43944d2c/out-of-service.mt-4f8.1719319299839.race";
        List<LeaderboardEntry.Penalty> penaltiesFromReplay = getPenaltiesFromReplay(replayUrl);
        System.out.println("");
    }

    public static List<LeaderboardEntry.Penalty> getPenaltiesFromReplay(String replayUrl) throws IOException, DataFormatException, EmptyReplayException {
        List<LeaderboardEntry.Penalty> penalties = new ArrayList<>();
        byte[] replayData = downloadReplay(replayUrl);
        if (replayData.length == 0) {
           throw new EmptyReplayException();
        }
        MarkerData markers = parseReplayData(replayData);
        sumMarkerData(markers);
        penalties = findBounces(markers);
        return penalties;
    }

    private static byte[] downloadReplay(String urlString) throws IOException {
        URL url = new URL(urlString);
        try (InputStream in = url.openStream()) {
            return in.readAllBytes();
        }
    }

    private static MarkerData parseReplayData(byte[] replayData) throws IOException, DataFormatException {
//        JsonNode jsonNode = extractJSON(new String(replayData));
        byte[] markerData = Arrays.copyOfRange(replayData, 0, replayData.length);
        return findMarkersAndExtractData(markerData, 0);
    }

    private static JsonNode extractJSON(String str) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(str.substring(str.indexOf('{'), str.lastIndexOf('}') + 1));
    }

    private static MarkerData findMarkersAndExtractData(byte[] byteArray, int offset) throws IOException, DataFormatException {
        Map<String, String> markerPrefixes = new LinkedHashMap<>() {{
            put("time,", "time");
            put("drone-px,", "drone-px");
            put("drone-py,", "drone-py");
            put("drone-pz,", "drone-pz");
            put("input-t", "input-t");
            put("drone-vx", "drone-vx");
            put("drone-vy", "drone-vy");
            put("drone-vz", "drone-vz");
            put("drone-rpm0", "drone-rpm0");
            put("drone-rpm1", "drone-rpm1");
            put("drone-rpm2", "drone-rpm2");
            put("drone-rpm3", "drone-rpm3");
        }};

        MarkerData output = new MarkerData();
        Map<String, List<Float>> extractedData = new HashMap<>();

        List<String> markerKeys = new ArrayList<>(markerPrefixes.keySet());
        int currentMarkerIndex = 0;
        int byteIndex = offset;
        String markerPrefix = markerKeys.get(currentMarkerIndex);

        while (byteIndex < byteArray.length && currentMarkerIndex < markerKeys.size()) {
            if (byteIndex < byteArray.length - 10 &&
                    byteArray[byteIndex] == markerPrefix.charAt(0)
                    && byteArray[byteIndex+1] == markerPrefix.charAt(1)
                    && byteArray[byteIndex+2] == markerPrefix.charAt(2)
                    && byteArray[byteIndex+3] == markerPrefix.charAt(3)
                    && byteArray[byteIndex+4] == markerPrefix.charAt(4)
            ) {
                String possibleMarker = new String(Arrays.copyOfRange(byteArray, byteIndex, byteIndex + 20));
                if (possibleMarker.startsWith(markerPrefix)) {
                    String[] splittedMarker = possibleMarker.split(",");
                    int bytePartLength = Integer.parseInt(splittedMarker[1]);
                    int markerPartLength = possibleMarker.indexOf(",4,1") + 5;
                    byte[] compressedData = Arrays.copyOfRange(byteArray, byteIndex + markerPartLength, byteIndex + markerPartLength + bytePartLength);
                    byte[] unzippedBytePart;
                    try {
                        unzippedBytePart = decompress(compressedData);
                    } catch (ZipException e){
                        LOG.warn("This is some pretty stupid bug, where the part isn't actually gzipped at all");
                        unzippedBytePart = compressedData;
                    }
                    extractedData.put(markerPrefixes.get(markerPrefix), byteArrayToFloatList(unzippedBytePart));
                    currentMarkerIndex++;
                    byteIndex += markerPartLength + bytePartLength;
                    if (currentMarkerIndex < markerKeys.size()) {
                        markerPrefix = markerKeys.get(currentMarkerIndex);
                    }
                    continue;
                }
            }
            byteIndex++;
        }

        output.time = extractedData.getOrDefault("time", new ArrayList<>());
        output.dronePosX = extractedData.getOrDefault("drone-px", new ArrayList<>());
        output.dronePosY = extractedData.getOrDefault("drone-py", new ArrayList<>());
        output.dronePosZ = extractedData.getOrDefault("drone-pz", new ArrayList<>());
        output.droneVX = extractedData.getOrDefault("drone-vx", new ArrayList<>());
        output.droneVY = extractedData.getOrDefault("drone-vy", new ArrayList<>());
        output.droneVZ = extractedData.getOrDefault("drone-vz", new ArrayList<>());
        output.inputT = extractedData.getOrDefault("input-t", new ArrayList<>());

        return output;
    }

    private static byte[] decompress(byte[] compressedData) throws IOException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);
             GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = gzipInputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            return byteArrayOutputStream.toByteArray();
        }
    }

    private static List<Float> byteArrayToFloatList(byte[] byteArray) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        List<Float> floatList = new ArrayList<>();
        while (floatBuffer.hasRemaining()) {
            floatList.add(floatBuffer.get());
        }
        return floatList;
    }

    private static void sumMarkerData(MarkerData markers) {
        sumFloatList(markers.time);
        sumFloatList(markers.dronePosX);
        sumFloatList(markers.dronePosY);
        sumFloatList(markers.dronePosZ);
        sumFloatList(markers.droneVX);
        sumFloatList(markers.droneVY);
        sumFloatList(markers.droneVZ);
        sumFloatList(markers.inputT);

        calculateDroneVelocityAndDirectionChanges(markers);
    }

    private static void calculateDroneVelocityAndDirectionChanges(MarkerData markers) {
        List<Float> droneVelocity = new ArrayList<>();
        List<Double> directionChanges = new ArrayList<>();

        for (int i = 0; i < markers.droneVX.size(); i++) {
            float vx = markers.droneVX.get(i);
            float vy = markers.droneVY.get(i);
            float vz = markers.droneVZ.get(i);
            droneVelocity.add((float) Math.sqrt(vx * vx + vy * vy + vz * vz));

            if (i > 0) {
                float previousVX = markers.droneVX.get(i - 1);
                float previousVY = markers.droneVY.get(i - 1);
                float previousVZ = markers.droneVZ.get(i - 1);

                double dotProduct = previousVX * vx + previousVY * vy + previousVZ * vz;
                double previousMagnitude = Math.sqrt(previousVX * previousVX + previousVY * previousVY + previousVZ * previousVZ);
                double currentMagnitude = Math.sqrt(vx * vx + vy * vy + vz * vz);

                double angle = Math.acos(dotProduct / (previousMagnitude * currentMagnitude));
                directionChanges.add(angle);
            } else {
                directionChanges.add(0.0);
            }
        }

        markers.droneVelocity = droneVelocity;
        markers.directionChanges = directionChanges;
    }

    private static void sumFloatList(List<Float> list) {
        for (int i = 1; i < list.size(); i++) {
            list.set(i, list.get(i) + list.get(i - 1));
        }
    }

    private static List<LeaderboardEntry.Penalty> findBounces(MarkerData markers) {
        final double lowestThresholdLimit = 5;
        final double aboveCountsAsHighVelocity = 15;
        final double directionChangeThresholdLowSpeed = Math.toRadians(30);
        final double directionChangeThresholdHighSpeed = Math.toRadians(16);
        List<LeaderboardEntry.Penalty> bouncePenalties = new ArrayList<>();
        // Calculate max velocity
        float maxVelocity = markers.droneVelocity.stream().max(Float::compare).orElse(0f);
        float maxInputT = markers.inputT.stream().max(Float::compare).orElse(0f);
        final float maxInputTLimit = maxInputT * 0.85f;
        double endingTime = markers.time.get(markers.time.size() - 1);

        // Find bounces
        for (int i = 1; markers.time.get(i) < endingTime - 1.5; i++) {
            if(markers.time.get(i) < 1) continue;

            float prevVelocity = markers.droneVelocity.get(i-1);
            float currentVelocity = markers.droneVelocity.get(i-1);
            double directionChangeThreshold = prevVelocity > aboveCountsAsHighVelocity ? directionChangeThresholdHighSpeed : directionChangeThresholdLowSpeed;
            float avgThrottleAtBounce = (markers.inputT.get(i) + markers.inputT.get(i-5) + markers.inputT.get(i+5)) / 3;
            if (markers.directionChanges.get(i) > directionChangeThreshold
                    && prevVelocity >= lowestThresholdLimit
                    && currentVelocity >= lowestThresholdLimit
                    && avgThrottleAtBounce > maxInputTLimit
            ) {
                double directionChangeInDegrees = Math.toDegrees(markers.directionChanges.get(i));
                System.out.println("Found bounce: " + i + " directional change " + directionChangeInDegrees + " at time " + markers.time.get(i));
                LeaderboardEntry.Penalty.Bounce bounce = new LeaderboardEntry.Penalty.Bounce((float) directionChangeInDegrees, prevVelocity, markers.droneVelocity.get(i));
                LeaderboardEntry.Penalty penalty = new LeaderboardEntry.Penalty(LeaderboardEntry.Penalty.Type.BOUNCE, bounce, 1000, (long) (1000 * markers.time.get(i)));
                bouncePenalties.add(penalty);
            }
        }

        return bouncePenalties;
    }
}

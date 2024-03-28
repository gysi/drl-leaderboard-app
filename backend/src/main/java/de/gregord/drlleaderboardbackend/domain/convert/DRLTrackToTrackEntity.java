package de.gregord.drlleaderboardbackend.domain.convert;

import de.gregord.drlleaderboardbackend.entities.Track;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class DRLTrackToTrackEntity {
    private static final Map<String, String> mapIdToMapName = new HashMap<>();

    static {
        mapIdToMapName.put("MP-3fd", "HARD ROCK STADIUM");
        mapIdToMapName.put("MP-693", "CHAMPIONSHIP KINGDOM");
        mapIdToMapName.put("MP-2e1", "ALLIANZ RIVIERA");
        mapIdToMapName.put("MP-3c7", "BMW WELT");
        mapIdToMapName.put("MP-8e5", "ADVENTUREDOME");
        mapIdToMapName.put("MP-0a3", "BIOSPHERE 2");
        mapIdToMapName.put("MP-639", "SKATEPARK LA");
        mapIdToMapName.put("MP-46c", "CALIFORNIA NIGHTS");
        mapIdToMapName.put("MP-342", "2017 WORLD CHAMPIONSHIP");
        mapIdToMapName.put("MP-669", "MUNICH PLAYOFFS");
        mapIdToMapName.put("MP-0b3", "BOSTON FOUNDRY");
        mapIdToMapName.put("MP-df5", "MARDI GRAS WORLD");
        mapIdToMapName.put("MP-ed5", "DETROIT");
        mapIdToMapName.put("MP-7ed", "ATLANTA AFTERMATH");
        mapIdToMapName.put("MP-a35", "OHIO CRASHSITE");
        mapIdToMapName.put("MP-92e", "PROJECT MANHATTAN");
        mapIdToMapName.put("MP-ad9", "MIAMI LIGHTS");
        mapIdToMapName.put("MP-bf7", "L.A.POCALYPSE");
        mapIdToMapName.put("MP-2cb", "MEGA CITY");
        mapIdToMapName.put("MP-f95", "U.S. AIR FORCE BONEYARD");
        mapIdToMapName.put("MP-19c", "U.S. AIR FORCE NIGHT MODE");
        mapIdToMapName.put("MP-95a", "GATES OF NEW YORK");
        mapIdToMapName.put("MP-50c", "CAMPGROUND");
        mapIdToMapName.put("MP-615", "DRONE PARK");
        mapIdToMapName.put("MP-103", "OUT OF SERVICE");
        mapIdToMapName.put("MP-409", "THE HOUSE");
        mapIdToMapName.put("MP-23c", "DRL SANDBOX");
        mapIdToMapName.put("MP-b59", "BRIDGE");
        mapIdToMapName.put("MP-0c6", "MULTIGP");
        mapIdToMapName.put("MP-cb7", "SILICON VALLEY");
    }

    public static void setValuesToTrack(Map<String, Object> apiEntry, Track track) {
        track.setGuid((String) apiEntry.get("guid"));
        track.setName(((String) apiEntry.get("map-title")).trim().toUpperCase());
        track.setMapId((String) apiEntry.get("map-id"));
        track.setMapCategory((String) apiEntry.get("map-category"));
        track.setMapName(mapIdToMapName.get(track.getMapId()));
        track.setCreatedAt(LocalDateTime.from(ZonedDateTime.parse((String) apiEntry.get("created-at"))));
        track.setUpdatedAt(LocalDateTime.from(ZonedDateTime.parse((String) apiEntry.get("updated-at"))));
//        track.setIsDrlOfficial((Boolean) apiEntry.get("is-drl-official"));
//        track.setIsPublic((Boolean) apiEntry.get("is-public"));
        track.setMapDifficulty((Integer) apiEntry.get("map-difficulty"));
        track.setMapLaps((Integer) apiEntry.get("map-laps"));
        track.setMapDistance(((Number) apiEntry.get("map-distance")).doubleValue());
//        List<String> categories = (List<String>) apiEntry.get("categories");
//        if (categories.size() > 0) {
//            track.setCategories(categories.get(0));
//        }
        track.setTrackCreator((String) apiEntry.get("profile-name"));
        track.setMapThumb((String) apiEntry.get("map-thumb"));
        track.setRatingScore(((Number) apiEntry.get("score")).doubleValue());
        track.setRatingCount((Integer) apiEntry.get("rating-count"));
    }

    public static String getMapNameFromMapId(String mapId) {
        return mapIdToMapName.get(mapId);
    }
}

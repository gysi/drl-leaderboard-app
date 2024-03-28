package de.gregord.drlleaderboardbackend.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.gregord.drlleaderboardbackend.domain.serializer.MapCategorySerializer;

import java.util.*;

@JsonSerialize(using = MapCategorySerializer.class)
public enum MapCategory {
    MapDRL("DRL Maps"),
    MapVirtualSeason("Virutal Season"),
    MapMultiGP("MultiGP"),
    MapDRLSimCup("Sim Racing Cup"),
    MapFeatured("Featured"),
    MapOriginal("Originals"),
    MapCommunity("Community");

    private static final Map<String, MapCategory> STRING_TO_ENUM = new HashMap<String, MapCategory>();
    private static final Set<MapCategory> officalCategories;
    private static final Set<Integer> officalCategoriesIds;
    private static final Set<MapCategory> communityCategories;
    private static final Set<Integer> communityCategoriesIds;

    static {
        for (MapCategory mapCategory : values()) {
            STRING_TO_ENUM.put(mapCategory.name(), mapCategory);
        }

        officalCategories = Set.of(
                MapDRL,
                MapVirtualSeason,
                MapMultiGP,
                MapDRLSimCup,
                MapFeatured,
                MapOriginal
        );

        officalCategoriesIds = Set.of(
                MapDRL.ordinal(),
                MapVirtualSeason.ordinal(),
                MapMultiGP.ordinal(),
                MapDRLSimCup.ordinal(),
                MapFeatured.ordinal(),
                MapOriginal.ordinal()
        );

        communityCategories = Set.of(
                MapCommunity
        );

        communityCategoriesIds = Set.of(
                MapCommunity.ordinal()
        );
    }

    private final String description;

    MapCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Optional<MapCategory> fromString(String name) {
        return Optional.ofNullable(STRING_TO_ENUM.get(name));
    }

    public static Set<MapCategory> getOfficialCategories() {
        return officalCategories;
    }

    public static Set<Integer> getOfficialCategoriesIds() {
        return officalCategoriesIds;
    }

    public static Set<MapCategory> getCommunityCategories() {
        return communityCategories;
    }

    public static Set<Integer> getCommunityCategoriesIds() {
        return communityCategoriesIds;
    }
}

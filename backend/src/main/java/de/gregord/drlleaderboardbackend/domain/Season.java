package de.gregord.drlleaderboardbackend.domain;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.NavigableMap;
import java.util.stream.Collectors;
import java.util.TreeMap;

public enum Season {
    // Spring: March 1 - May 31
    // 2023 no spring season because of to little tournaments because of DRL game release
//        SEASON_2023_SPRING("2023-01-SPRING", "Spring Season 2023",
//                LocalDateTime.of(2023,3,1, 0, 0),
//                LocalDateTime.of(2023,6,1, 0, 0)),
    // Summer: June 1 - August 31
    SEASON_2023_SUMMER("2023-02-SUMMER", "Summer Season 2023",
            LocalDateTime.of(2023,5,1, 0, 0), // We count a little bit earlier to include tournaments that start in May
            LocalDateTime.of(2023,9,1, 0, 0)),
    // Fall/Autumn: September 1 - November 30
    SEASON_2023_FALL("2023-03-FALL", "Fall Season 2023",
            LocalDateTime.of(2023,9,1, 0, 0),
            LocalDateTime.of(2023,12,1, 0, 0)),
    // Winter: December 1 - February 28 (or February 29 in a leap year)
    SEASON_2023_WINTER("2023-04-WINTER", "Winter Season 2023/24",
            LocalDateTime.of(2023,12,1, 0, 0),
            LocalDateTime.of(2024,3,1, 0, 0));

    private final String seasonId;
    private final String seasonName;
    private final LocalDateTime seasonStartDate; // inclusive
    private final LocalDateTime seasonEndDate; // exclusive

    public static final Map<String, Season> SEASON_MAPPING_BY_SEASON_ID = Arrays.stream(Season.values())
            .collect(Collectors.toMap(Season::getSeasonId, season -> season));

    public static final NavigableMap<LocalDateTime, Season> SEASON_MAPPING_BY_DATE = Arrays.stream(Season.values())
            .collect(Collectors.toMap(Season::getSeasonStartDate, season -> season, (oldValue, newValue) -> oldValue, TreeMap::new));

    Season(String seasonId, String seasonName, LocalDateTime seasonStartDate, LocalDateTime seasonEndDate) {
        this.seasonId = seasonId;
        this.seasonName = seasonName;
        this.seasonStartDate = seasonStartDate;
        this.seasonEndDate = seasonEndDate;
    }

    public String getSeasonId() {
        return seasonId;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public LocalDateTime getSeasonStartDate() {
        return seasonStartDate;
    }
    public LocalDateTime getSeasonEndDate() {
        return seasonEndDate;
    }
}

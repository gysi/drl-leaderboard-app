package de.gregord.drlleaderboardbackend.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.CheckForNull;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public enum Season {
// 2023 no spring season because of to little tournaments because of DRL game release
//        SEASON_2023_SPRING("2023-01-SPRING", "Spring Season 2023",
//                LocalDateTime.of(2023,3,1, 0, 0),
//                LocalDateTime.of(2023,6,1, 0, 0)),
    SEASON_2023_SUMMER("2023-02-SUMMER", "Summer Season 2023",
            LocalDateTime.of(2023,5,1, 0, 0), // We count a little bit earlier to include tournaments that start in May
            LocalDateTime.of(2023,9,1, 0, 0)),
    SEASON_2023_FALL("2023-03-FALL", "Fall Season 2023",
            LocalDateTime.of(2023,9,1, 0, 0),
            LocalDateTime.of(2023,12,1, 0, 0)),
    SEASON_2023_WINTER("2023-04-WINTER", "Winter Season 2023/24",
            LocalDateTime.of(2023,12,1, 0, 0),
            LocalDateTime.of(2024,3,1, 0, 0)),
    SEASON_2024_SPRING("2024-01-SPRING", "Spring Season 2024",
            LocalDateTime.of(2024,3,1, 0, 0),
            LocalDateTime.of(2024,6,1, 0, 0)),
    SEASON_2024_SUMMER("2024-02-SUMMER", "Summer Season 2024",
            LocalDateTime.of(2024,6,1, 0, 0),
            LocalDateTime.of(2024,9,1, 0, 0)) {
        {
            this.details_v1 = new Details_V1();
            this.details_v1.hasPrizePool = true;
            this.details_v1.hasQualification = false;
            this.details_v1.prizePool.add("$509");
            this.details_v1.prizePool.add("$363");
            this.details_v1.prizePool.add("$247");
            this.details_v1.prizePool.add("$189");
            this.details_v1.prizePool.add("$145");
            this.details_v1.format = Details_V1.Format.QUAL_TIME_TRIAL_FINISH_TIME_TRIAL;
            this.details_v1.matcherino = new Details_V1.Matcherino();
            this.details_v1.matcherino.eventId = "116263";
            this.details_v1.matcherino.matcherinoEventLink = "https://matcherino.com/tournaments/116263";
            this.details_v1.matcherino.promoBannerImageName = "background-Summer_Series_2024_matcherino_register";

        }
    },
    SEASON_2024_FALL("2024-03-FALL", "Fall Season 2024",
            LocalDateTime.of(2024,9,16, 0, 0),
            LocalDateTime.of(2024,12,1, 0, 0))
            {
                {
                    this.details_v1 = new Details_V1();
                    this.details_v1.hasPrizePool = true;
                    this.details_v1.hasQualification = true;
                    this.details_v1.numberOfQualifications = 24;
                    this.details_v1.prizePool.add("$400");
                    this.details_v1.prizePool.add("$250");
                    this.details_v1.prizePool.add("$150");
                    this.details_v1.prizePool.add("$100");
                    this.details_v1.prizePool.add("$50");
                    this.details_v1.prizePool.add("$50");
                    this.details_v1.format = Details_V1.Format.QUAL_TIME_TRAIL_FINISH_TOURNAMENT;
                    this.details_v1.matcherino = new Details_V1.Matcherino();
                    this.details_v1.matcherino.eventId = "drl-community-fall-season-2024";
                    this.details_v1.matcherino.matcherinoEventLink = "https://matcherino.com/t/drl-community-fall-season-2024";
                    // TODO add banner!
//                    this.details_v1.matcherino.promoBannerImageName = "background-Summer_Series_2024_matcherino_register";
                }
            },
    SEASON_2024_WINTER("2024-04-WINTER", "Winter Season 2024/25",
            LocalDateTime.of(2024,12,1, 0, 0),
            LocalDateTime.of(2025,3,1, 0, 0)),
    SEASON_2025_SPRING("2025-01-SPRING", "Spring Season 2025",
            LocalDateTime.of(2025,3,1, 0, 0),
            LocalDateTime.of(2025,6,1, 0, 0)),
    SEASON_2025_SUMMER("2025-02-SUMMER", "Summer Season 2025",
            LocalDateTime.of(2025,6,1, 0, 0),
            LocalDateTime.of(2025,9,1, 0, 0)),
    SEASON_2025_FALL("2025-03-FALL", "Fall Season 2025",
            LocalDateTime.of(2025,9,1, 0, 0),
            LocalDateTime.of(2025,12,1, 0, 0)),
    SEASON_2025_WINTER("2025-04-WINTER", "Winter Season 2025/2026",
            LocalDateTime.of(2025,12,1, 0, 0),
            LocalDateTime.of(2026,3,1, 0, 0)),
    SEASON_2026_SPRING("2026-01-SPRING", "Spring Season 2026",
            LocalDateTime.of(2026,3,1, 0, 0),
            LocalDateTime.of(2026,6,1, 0, 0)),
    SEASON_2026_SUMMER("2026-02-SUMMER", "Summer Season 2026",
            LocalDateTime.of(2026,6,1, 0, 0),
            LocalDateTime.of(2026,9,1, 0, 0)),
    SEASON_2026_FALL("2026-03-FALL", "Fall Season 2026",
            LocalDateTime.of(2026,9,1, 0, 0),
            LocalDateTime.of(2026,12,1, 0, 0)),
    SEASON_2026_WINTER("2026-04-WINTER", "Winter Season 2026/2027",
            LocalDateTime.of(2026,12,1, 0, 0),
            LocalDateTime.of(2027,3,1, 0, 0)),
    SEASON_2027_SPRING("2027-01-SPRING", "Spring Season 2027",
            LocalDateTime.of(2027,3,1, 0, 0),
            LocalDateTime.of(2027,6,1, 0, 0)),
    SEASON_2027_SUMMER("2027-02-SUMMER", "Summer Season 2027",
            LocalDateTime.of(2027,6,1, 0, 0),
            LocalDateTime.of(2027,9,1, 0, 0)),
    SEASON_2027_FALL("2027-03-FALL", "Fall Season 2027",
            LocalDateTime.of(2027,9,1, 0, 0),
            LocalDateTime.of(2027,12,1, 0, 0)),
    SEASON_2027_WINTER("2027-04-WINTER", "Winter Season 2027/2028",
            LocalDateTime.of(2027,12,1, 0, 0),
            LocalDateTime.of(2028,3,1, 0, 0)),
    SEASON_2028_SPRING("2028-01-SPRING", "Spring Season 2028",
            LocalDateTime.of(2028,3,1, 0, 0),
            LocalDateTime.of(2028,6,1, 0, 0)),
    SEASON_2028_SUMMER("2028-02-SUMMER", "Summer Season 2028",
            LocalDateTime.of(2028,6,1, 0, 0),
            LocalDateTime.of(2028,9,1, 0, 0)),
    SEASON_2028_FALL("2028-03-FALL", "Fall Season 2028",
            LocalDateTime.of(2028,9,1, 0, 0),
            LocalDateTime.of(2028,12,1, 0, 0)),
    SEASON_2028_WINTER("2028-04-WINTER", "Winter Season 2028/2029",
            LocalDateTime.of(2028,12,1, 0, 0),
            LocalDateTime.of(2029,3,1, 0, 0)),
    SEASON_2029_SPRING("2029-01-SPRING", "Spring Season 2029",
            LocalDateTime.of(2029,3,1, 0, 0),
            LocalDateTime.of(2029,6,1, 0, 0)),
    SEASON_2029_SUMMER("2029-02-SUMMER", "Summer Season 2029",
            LocalDateTime.of(2029,6,1, 0, 0),
            LocalDateTime.of(2029,9,1, 0, 0)),
    SEASON_2029_FALL("2029-03-FALL", "Fall Season 2029",
            LocalDateTime.of(2029,9,1, 0, 0),
            LocalDateTime.of(2029,12,1, 0, 0)),
    SEASON_2029_WINTER("2029-04-WINTER", "Winter Season 2029/2030",
            LocalDateTime.of(2029,12,1, 0, 0),
            LocalDateTime.of(2030,3,1, 0, 0)),
    SEASON_2030_SPRING("2030-01-SPRING", "Spring Season 2030",
            LocalDateTime.of(2030,3,1, 0, 0),
            LocalDateTime.of(2030,6,1, 0, 0)),
    SEASON_2030_SUMMER("2030-02-SUMMER", "Summer Season 2030",
            LocalDateTime.of(2030,6,1, 0, 0),
            LocalDateTime.of(2030,9,1, 0, 0)),
    SEASON_2030_FALL("2030-03-FALL", "Fall Season 2030",
            LocalDateTime.of(2030,9,1, 0, 0),
            LocalDateTime.of(2030,12,1, 0, 0)),
    SEASON_2030_WINTER("2030-04-WINTER", "Winter Season 2030/2031",
            LocalDateTime.of(2030,12,1, 0, 0),
            LocalDateTime.of(2031,3,1, 0, 0)),
    SEASON_2031_SPRING("2031-01-SPRING", "Spring Season 2031",
            LocalDateTime.of(2031,3,1, 0, 0),
            LocalDateTime.of(2031,6,1, 0, 0)),
    SEASON_2031_SUMMER("2031-02-SUMMER", "Summer Season 2031",
            LocalDateTime.of(2031,6,1, 0, 0),
            LocalDateTime.of(2031,9,1, 0, 0)),
    SEASON_2031_FALL("2031-03-FALL", "Fall Season 2031",
            LocalDateTime.of(2031,9,1, 0, 0),
            LocalDateTime.of(2031,12,1, 0, 0)),
    SEASON_2031_WINTER("2031-04-WINTER", "Winter Season 2031/2032",
            LocalDateTime.of(2031,12,1, 0, 0),
            LocalDateTime.of(2032,3,1, 0, 0)),
    SEASON_2032_SPRING("2032-01-SPRING", "Spring Season 2032",
            LocalDateTime.of(2032,3,1, 0, 0),
            LocalDateTime.of(2032,6,1, 0, 0)),
    SEASON_2032_SUMMER("2032-02-SUMMER", "Summer Season 2032",
            LocalDateTime.of(2032,6,1, 0, 0),
            LocalDateTime.of(2032,9,1, 0, 0)),
    SEASON_2032_FALL("2032-03-FALL", "Fall Season 2032",
            LocalDateTime.of(2032,9,1, 0, 0),
            LocalDateTime.of(2032,12,1, 0, 0)),
    SEASON_2032_WINTER("2032-04-WINTER", "Winter Season 2032/2033",
            LocalDateTime.of(2032,12,1, 0, 0),
            LocalDateTime.of(2033,3,1, 0, 0)),
    SEASON_2033_SPRING("2033-01-SPRING", "Spring Season 2033",
            LocalDateTime.of(2033,3,1, 0, 0),
            LocalDateTime.of(2033,6,1, 0, 0)),
    SEASON_2033_SUMMER("2033-02-SUMMER", "Summer Season 2033",
            LocalDateTime.of(2033,6,1, 0, 0),
            LocalDateTime.of(2033,9,1, 0, 0)),
    SEASON_2033_FALL("2033-03-FALL", "Fall Season 2033",
            LocalDateTime.of(2033,9,1, 0, 0),
            LocalDateTime.of(2033,12,1, 0, 0)),
    SEASON_2033_WINTER("2033-04-WINTER", "Winter Season 2033/2034",
            LocalDateTime.of(2033,12,1, 0, 0),
            LocalDateTime.of(2034,3,1, 0, 0)),
    SEASON_2034_SPRING("2034-01-SPRING", "Spring Season 2034",
            LocalDateTime.of(2034,3,1, 0, 0),
            LocalDateTime.of(2034,6,1, 0, 0)),
    SEASON_2034_SUMMER("2034-02-SUMMER", "Summer Season 2034",
            LocalDateTime.of(2034,6,1, 0, 0),
            LocalDateTime.of(2034,9,1, 0, 0)),
    SEASON_2034_FALL("2034-03-FALL", "Fall Season 2034",
            LocalDateTime.of(2034,9,1, 0, 0),
            LocalDateTime.of(2034,12,1, 0, 0)),
    SEASON_2034_WINTER("2034-04-WINTER", "Winter Season 2034/2035",
            LocalDateTime.of(2034,12,1, 0, 0),
            LocalDateTime.of(2035,3,1, 0, 0)),
    NO_SEASON("NO-SEASON", "No Season",
                       LocalDateTime.of(3000,1,1, 0, 0),
            LocalDateTime.of(3000,12,1, 0, 0));

    private static final Logger LOG = LoggerFactory.getLogger(Season.class);
    private final String seasonIdName;
    private final String seasonName;
    private final LocalDateTime seasonStartDate; // inclusive
    private final LocalDateTime seasonEndDate; // exclusive
    Details_V1 details_v1 = null;

    public static final Map<String, Season> SEASON_MAPPING_BY_SEASON_ID_NAME = Arrays.stream(Season.values())
            .collect(Collectors.toMap(Season::getSeasonIdName, season -> season));

    public static final NavigableMap<LocalDateTime, Season> SEASON_MAPPING_BY_DATE = Arrays.stream(Season.values())
            .collect(Collectors.toMap(Season::getSeasonStartDate, season -> season, (oldValue, newValue) -> oldValue, TreeMap::new));

    Season(String seasonIdName, String seasonName, LocalDateTime seasonStartDate, LocalDateTime seasonEndDate) {
        this.seasonIdName = seasonIdName;
        this.seasonName = seasonName;
        this.seasonStartDate = seasonStartDate;
        this.seasonEndDate = seasonEndDate;
    }

    public int getId() {
        if(this == NO_SEASON) {
            return -1;
        }
        return ordinal();
    }

    public int getSeasonId(){
        return getId();
    }

    public String getSeasonIdName() {
        return seasonIdName;
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

    public Details_V1 getDetails_v1() {
        return this.details_v1;
    }

    public static int getCurrentSeasonId() {
        return getCurrentSeason().getId();
    }

    public static String getCurrentSeasonIdName() {
        return getCurrentSeason().getSeasonIdName();
    }

    public static Season getCurrentSeason() {
        Season currentSeason = SEASON_MAPPING_BY_DATE.floorEntry(LocalDateTime.now()).getValue();
        if(currentSeason.getSeasonEndDate().isBefore(LocalDateTime.now())) {
            return NO_SEASON;
        }
        return currentSeason;
    }

    @CheckForNull
    public static Season getPreviousSeason(Season season) {
        if(season == NO_SEASON){
            season = SEASON_MAPPING_BY_DATE.floorEntry(LocalDateTime.now()).getValue();
            if (season == null){
                return NO_SEASON;
            } else {
                return season;
            }
        }
        int seasonOrdinal = season.ordinal();
        if(seasonOrdinal == 0){
            LOG.warn("There is no previous season before season: {}", season);
            return NO_SEASON;
        }
        return Season.values()[season.ordinal() - 1];
    }

    @CheckForNull
    public static Season getPreviousSeason() {
        return getPreviousSeason(getCurrentSeason());
    }

    @CheckForNull
    public static Season getNextSeason(Season season) {
        if(season == NO_SEASON){
            season = SEASON_MAPPING_BY_DATE.floorEntry(LocalDateTime.now()).getValue();
        }
        int seasonOrdinal = season.ordinal();
        if(seasonOrdinal == Season.SEASON_2034_SUMMER.ordinal()){
            LOG.warn("There is no previous season after season: {}", season);
            return null;
        }
        return Season.values()[season.ordinal() + 1];
    }

    @CheckForNull
    public static Season getNextSeason() {
        return getNextSeason(getCurrentSeason());
    }

    @CheckForNull
    public static Season getBySeasionIdName(String seasonIdName) {
        return SEASON_MAPPING_BY_SEASON_ID_NAME.get(seasonIdName);
    }

    public static class Details_V1 {
        public Boolean hasPrizePool = false;
        public Boolean hasQualification = false;
        public Integer numberOfQualifications = 0;
        public List<String> prizePool = new ArrayList<>();
        public Format format;
        public Matcherino matcherino;

        public static enum Format {
            QUAL_TIME_TRIAL_FINISH_TIME_TRIAL,
            QUAL_TIME_TRAIL_FINISH_TOURNAMENT,
            QUAL_TIME_TRIAL_AND_TOURNAMENTS_FINISH_TOURNAMENT,
        }

        public static class Matcherino {
            public String eventId;
            public String matcherinoEventLink;
            public String promoBannerImageName;

        }
    }

    public static void main(String[] args) {
        System.out.println(Season.values()[0].ordinal());
    }
}

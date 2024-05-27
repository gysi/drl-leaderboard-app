package de.gregord.drlleaderboardbackend.dataupdater;

import de.gregord.drlleaderboardbackend.domain.Season;
import de.gregord.drlleaderboardbackend.domain.TrackWeightedRatingView;
import de.gregord.drlleaderboardbackend.entities.CommunitySeason;
import de.gregord.drlleaderboardbackend.entities.LeaderboardEntry;
import de.gregord.drlleaderboardbackend.entities.MapCategory;
import de.gregord.drlleaderboardbackend.entities.Track;
import de.gregord.drlleaderboardbackend.repositories.CommunitySeasonsRepository;
import de.gregord.drlleaderboardbackend.repositories.TracksRepository;
import de.gregord.drlleaderboardbackend.services.DRLApiService;
import de.gregord.drlleaderboardbackend.services.LeaderboardProcessorResult;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import static de.gregord.drlleaderboardbackend.config.CacheConfig.CACHE_COMMUNITY_CURRENT_SEASON_TRACKIDS;

@Component
public class CommunityTracksSeasonUpdater {
    private static final Logger LOG = LoggerFactory.getLogger(CommunityTracksSeasonUpdater.class);
    private static final int NEXT_SEASON_LEAD_PERIOD_DAYS = 14;

    private final DRLApiService drlApiService;
    private final TracksRepository tracksRepository;
    private final CommunitySeasonsRepository communitySeasonsRepository;
    private final ModelMapper modelMapper;

    public CommunityTracksSeasonUpdater(DRLApiService drlApiService,
                                        TracksRepository tracksRepository,
                                        CommunitySeasonsRepository communitySeasonsRepository,
                                        ModelMapper modelMapper) {
        this.drlApiService = drlApiService;
        this.tracksRepository = tracksRepository;
        this.communitySeasonsRepository = communitySeasonsRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @CacheEvict(CACHE_COMMUNITY_CURRENT_SEASON_TRACKIDS)
    public void updateCommunitySeasonTracks() {
        final int pastSeasonTracksLimit = 25;
        final int bestRatedSeasonTracksLimit = 5;
        Season currentSeason = Season.getCurrentSeason();
        LocalDateTime currentSeasonEndDate = currentSeason.getSeasonEndDate();
        LocalDateTime now = LocalDateTime.now();
        boolean isPreviewSeason = currentSeasonEndDate.minusDays(NEXT_SEASON_LEAD_PERIOD_DAYS).isBefore(now);

        Season seasonToProcess = isPreviewSeason ? Season.getNextSeason() : Season.getCurrentSeason();
        if (seasonToProcess == null) {
            LOG.error("can't updateCommunitySeasonTracks because the Season is null!");
            return;
        }
        List<CommunitySeason> communitySeasonList =
                communitySeasonsRepository.findCommunitySeasonBySeasonIdAndExcludedIsFalse(seasonToProcess.getSeasonId());
        if (communitySeasonList.size() >= pastSeasonTracksLimit + bestRatedSeasonTracksLimit) {
            LOG.info("No need to create new season tracks because there are already enough: {}",
                    pastSeasonTracksLimit + bestRatedSeasonTracksLimit);
            return;
        }
        int tracksLeftToProcess = (pastSeasonTracksLimit + bestRatedSeasonTracksLimit) - communitySeasonList.size();
        Map<String, Integer> pickedTracksByCreator = new HashMap<>();
        Set<Long> pickedTracksIds = new HashSet<>();
        List<String> resultPrints = new ArrayList<>();

        // Find 25 eligible tracks from previous seasons
        if (tracksLeftToProcess > bestRatedSeasonTracksLimit) {
            selectTracksFromPreviousSeasons(tracksLeftToProcess - bestRatedSeasonTracksLimit,
                    seasonToProcess, pickedTracksByCreator, pickedTracksIds, resultPrints);
        }

        // Find 5 highest-rated community tracks
        selectHighestRatedTracks(tracksLeftToProcess - Math.max(0, (tracksLeftToProcess - bestRatedSeasonTracksLimit))
                , pickedTracksByCreator, pickedTracksIds, resultPrints);

        logPickedTracks(resultPrints);
        List<CommunitySeason> communitySeasons = new ArrayList<>();
        for (Long pickedTracksId : pickedTracksIds) {
            CommunitySeason communitySeason = new CommunitySeason();
            communitySeason.setSeasonId(seasonToProcess.getSeasonId());
            Track track = new Track();
            track.setId(pickedTracksId);
            communitySeason.setTrack(track);
            communitySeason.setSeasonIdName(seasonToProcess.getSeasonIdName());
            communitySeasons.add(communitySeason);
        }
        communitySeasonsRepository.saveAll(communitySeasons);
    }

    private void selectTracksFromPreviousSeasons(int limit,
                                                 Season seasonToGetTracksFrom,
                                                 Map<String, Integer> pickedTracksByCreator,
                                                 Set<Long> pickedTrackIds,
                                                 List<String> resultPrints) {
        AtomicInteger communityTracksFromLastSeasonFound = new AtomicInteger();
        if (seasonToGetTracksFrom == null) {
            LOG.warn("There is no current season, so we can't pick any track for the current one, this shouldn't happen ever!");
            return;
        }

        while (communityTracksFromLastSeasonFound.get() < limit) {
            seasonToGetTracksFrom = Season.getPreviousSeason(seasonToGetTracksFrom);
            if (seasonToGetTracksFrom == null) {
                LOG.warn("No more previous seasons to pick tracks from.");
                break;
            }

            LocalDateTime lowerLimit = seasonToGetTracksFrom.getSeasonStartDate();
            LocalDateTime upperLimit = seasonToGetTracksFrom.getSeasonEndDate();
            Double averageRatingWithinDateRange = tracksRepository.getAverageRatingWithinDateRange(
                    MapCategory.MapCommunity.ordinal(),
                    lowerLimit,
                    upperLimit);

            List<TrackWeightedRatingView> tracks = new ArrayList<>(tracksRepository.getTracksForDateRangeOrderedByRating(
                    MapCategory.MapCommunity.ordinal(),
                    lowerLimit,
                    upperLimit));

            LOG.info("Trying to find {} eligible tracks between {} and {}",
                    limit - communityTracksFromLastSeasonFound.get(),
                    lowerLimit, upperLimit);

            for (TrackWeightedRatingView track : tracks) {
                if (communityTracksFromLastSeasonFound.get() >= limit) {
                    break;
                }

                boolean passesFilters = trackNotAlreadyPicked(pickedTrackIds).test(track) &&
                        trackNameIsNotEmpty().test(track) &&
                        trackMaxFivePerCreator(pickedTracksByCreator).test(track) &&
                        trackRatingCountMin(10).test(track) &&
                        trackWeightedRatingMin(averageRatingWithinDateRange + 0.05).test(track) &&
                        trackNotAlreadyPickedInASeason().test(track) &&
                        trackScoreMax(100_000).test(track);

                if (passesFilters) {
                    processAndAddTrack(track, pickedTracksByCreator, pickedTrackIds, resultPrints);
                    communityTracksFromLastSeasonFound.getAndIncrement();
                }
            }
        }

        if (communityTracksFromLastSeasonFound.get() < limit) {
            LOG.warn("Only found {} tracks out of the requested {}.", communityTracksFromLastSeasonFound.get(), limit);
        }
    }


    private void selectHighestRatedTracks(int limit, Map<String, Integer> pickedTracksByCreator, Set<Long> pickedTrackIds,
                                          List<String> resultPrints) {
        LOG.info("Tyring to find {} eligible tracks over all the highest rated community tracks", limit);
        Double averageRating = tracksRepository.getAverageRating(MapCategory.MapCommunity.ordinal());
        tracksRepository.getTracksOrderedByRating(MapCategory.MapCommunity.ordinal())
                .filter(trackNotAlreadyPicked(pickedTrackIds))
                .filter(trackNameIsNotEmpty())
                .filter(trackMaxFivePerCreator(pickedTracksByCreator))
                .filter(trackRatingCountMin(50)) // Assuming 50 is the minimum rating count
                .filter(trackWeightedRatingMin(averageRating + 0.05))
                .filter(trackNotAlreadyPickedInASeason())
                .filter(trackScoreMax(100_000))
                .limit(limit)
                .forEach(track -> processAndAddTrack(track, pickedTracksByCreator, pickedTrackIds, resultPrints));
    }

    private void processAndAddTrack(TrackWeightedRatingView track, Map<String, Integer> pickedTracksByCreator, Set<Long> pickedTrackIds,
                                    List<String> resultPrints) {
        LOG.info("Picked: {}", trackWeightedRatingToString(track));
        pickedTracksByCreator.merge(track.getTrackCreator(), 1, Integer::sum);
        resultPrints.add(trackWeightedRatingToString(track));
        pickedTrackIds.add(track.getId());
    }

    private void logPickedTracks(List<String> resultPrints) {
        LOG.info("Picked tracks:");
        for (int i = 0; i < resultPrints.size(); i++) {
            LOG.info("{} {}", i, resultPrints.get(i));
        }
    }

    private Predicate<TrackWeightedRatingView> trackNotAlreadyPicked(Set<Long> pickedTrackGuids) {
        return trackWeightedRatingView -> {
            if (pickedTrackGuids.contains(trackWeightedRatingView.getId())) {
                LOG.info("Excluded because track was already picked: {}", trackWeightedRatingToString(trackWeightedRatingView));
                return false;
            }
            return true;
        };
    }

    private Predicate<TrackWeightedRatingView> trackNameIsNotEmpty() {
        return trackWeightedRatingView -> {
            if (trackWeightedRatingView.getName().trim().isEmpty()) {
                LOG.info("Excluded because track name is empty: {}", trackWeightedRatingToString(trackWeightedRatingView));
                return false;
            }
            return true;
        };
    }

    private Predicate<TrackWeightedRatingView> trackMaxFivePerCreator(Map<String, Integer> pickedTracksByCreator) {
        return trackWeightedRatingView -> {
            if (pickedTracksByCreator.getOrDefault(trackWeightedRatingView.getTrackCreator(), 0) >= 5) {
                LOG.info("Excluded because there are already 5 tracks for this creator: {}",
                        trackWeightedRatingToString(trackWeightedRatingView));
                return false;
            }
            return true;
        };
    }

    private Predicate<TrackWeightedRatingView> trackRatingCountMin(int minRatingCount) {
        return trackWeightedRatingView -> {
            if (trackWeightedRatingView.getRatingCount() < minRatingCount) {
                LOG.info("Excluded because rating count < {}: {}", minRatingCount, trackWeightedRatingToString(trackWeightedRatingView));
                return false;
            }
            return true;
        };
    }

    private Predicate<TrackWeightedRatingView> trackWeightedRatingMin(double minWeightedRating) {
        return track -> {
            if (track.getWeightedRating() < minWeightedRating) {
                LOG.info("Excluded because weighted rating < {}: {} < {}", minWeightedRating, trackWeightedRatingToString(track),
                        minWeightedRating);
                return false;
            }
            return true;
        };
    }

    private Predicate<TrackWeightedRatingView> trackNotAlreadyPickedInASeason() {
        return trackWeightedRatingView -> {
            boolean exists = communitySeasonsRepository.existsByTrackId(trackWeightedRatingView.getId());
            if (exists) {
                LOG.info("Excluded because track already picked in a previous season: {}",
                        trackWeightedRatingToString(trackWeightedRatingView));
                return false;
            }
            return true;
        };
    }

    private Predicate<TrackWeightedRatingView> trackScoreMax(long maxScore) {
        return trackWeightedRatingView -> {
            Long score = getScoreForTrack(trackWeightedRatingView);
            if (score == null || score > maxScore) {
                LOG.info("Excluded because score is null or score > {}: {} score:{}", maxScore,
                        trackWeightedRatingToString(trackWeightedRatingView), score);
                return false;
            }
            return true;
        };
    }

    private Long getScoreForTrack(TrackWeightedRatingView trackWeightedRatingView) {
        try {
            Track trackEntity = modelMapper.map(trackWeightedRatingView, Track.class);
            LeaderboardProcessorResult leaderboardProcessorResult = drlApiService.getAndProcessLeaderboardEntries(trackEntity, 1, 1, null);
            if (!leaderboardProcessorResult.getNewOrUpdatedLeaderboardEntries().isEmpty()) {
                LeaderboardEntry firstPositionLeaderboardEntry =
                        leaderboardProcessorResult.getNewOrUpdatedLeaderboardEntries().stream().findFirst().orElse(null);
                return firstPositionLeaderboardEntry.getScore();
            }
        } catch (Exception e) {
            LOG.error("Error retrieving score for track: {}", trackWeightedRatingView.getGuid(), e);
        }
        LOG.error("This here shouldn't really happen, except when there is no entry.");
        return null;
    }

    public static String trackWeightedRatingToString(TrackWeightedRatingView track) {
        return String.format("name:%s, trackCreator:%s, mapDifficulty:%s, ratingCount:%s, ratingScore:%s," +
                        " weightedRating:%s, tournamentBoost:%s",
                track.getName(), track.getTrackCreator(),
                track.getMapDifficulty(), track.getRatingCount(),
                track.getRatingScore(), track.getWeightedRating(),
                track.getTournamentBoost());
    }
}

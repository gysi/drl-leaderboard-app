package de.gregord.drlleaderboardbackend.services;

import de.gregord.drlleaderboardbackend.domain.LeaderboardByPlayerView;
import de.gregord.drlleaderboardbackend.domain.OverallRankingView;
import de.gregord.drlleaderboardbackend.entities.LeaderboardEntry;
import de.gregord.drlleaderboardbackend.entities.LeaderboardEntryMinimal;
import de.gregord.drlleaderboardbackend.entities.MapCategory;
import de.gregord.drlleaderboardbackend.entities.Track;
import de.gregord.drlleaderboardbackend.event.TrackLeaderboardUpdateEvent;
import de.gregord.drlleaderboardbackend.repositories.LeaderboardRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static de.gregord.drlleaderboardbackend.config.CacheConfig.CACHE_COMMUNITY_LEADERBOARD_BY_PLAYERNAME_CURRENT_SEASON;
import static de.gregord.drlleaderboardbackend.config.CacheConfig.CACHE_OFFICIAL_LEADERBOARD_BY_PLAYERNAME;

@Service
@Transactional(readOnly = true)
public class LeaderboardService {
    private static final Logger LOG = LoggerFactory.getLogger(LeaderboardService.class);
    private final ApplicationEventPublisher applicationEventPublisher;
    private final LeaderboardRepository leaderboardRepository;
    private final CommunitySeasonService communitySeasonService;
    private final ModelMapper modelMapper;

    public LeaderboardService(
            ApplicationEventPublisher applicationEventPublisher,
            LeaderboardRepository leaderboardRepository,
            CommunitySeasonService communitySeasonService,
            ModelMapper modelMapper
    ) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.leaderboardRepository = leaderboardRepository;
        this.communitySeasonService = communitySeasonService;
        this.modelMapper = modelMapper;
    }

    public List<LeaderboardEntry> findByTrackId(Long trackId) {
        return leaderboardRepository.findByTrackId(trackId);
    }

    @Transactional
    public void deleteBeatenByEntriesByLeaderboardId(Long leaderboardId) {
        leaderboardRepository.deleteBeatenByEntriesByLeaderboardId(leaderboardId);
    }

    @Transactional
    public void delete(LeaderboardEntry leaderboardEntry) {
        leaderboardRepository.delete(leaderboardEntry);
    }

    @Transactional
    public void saveAndDeleteLeaderboardEntriesForTrack(
            Track track,
            Collection<LeaderboardEntry> leaderboardEntriesToBeSaved,
            Collection<LeaderboardEntry> leaderboardEntriesUnchanged,
            Collection<LeaderboardEntry> leaderboardEntriesToBeDeleted
    ) {
        if(leaderboardEntriesToBeSaved.isEmpty() && leaderboardEntriesToBeDeleted.isEmpty()){
            return;
        }
        LOG.debug("Saving/Updating Leaderboard entries");
        List<LeaderboardEntry> leaderboardEntries = saveLeaderboardEntries(leaderboardEntriesToBeSaved);
        setBeatenByEntries(leaderboardEntries);
        deleteLeaderboardEntries(leaderboardEntriesToBeDeleted);

        List<LeaderboardEntry> allAffectedEntries = new ArrayList<>(leaderboardEntries);
        allAffectedEntries.addAll(leaderboardEntriesToBeDeleted);
        applicationEventPublisher.publishEvent(new TrackLeaderboardUpdateEvent(
                this,
                track,
                allAffectedEntries
        ));
    }

    @Transactional
    public List<LeaderboardEntry> saveLeaderboardEntries(Collection<LeaderboardEntry> leaderboardEntries) {
        return leaderboardRepository.saveAll(leaderboardEntries);
    }

    public void setBeatenByEntries(List<LeaderboardEntry> leaderboardEntries) {
        try (ExecutorService executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name("setBeatenByThread", 1L).factory())) {

            List<Callable<Void>> tasks = leaderboardEntries.stream()
                    .map(leaderboardEntry -> createSetBeatenByTask(leaderboardEntry, leaderboardEntries))
                    .collect(Collectors.toList());

            executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            LOG.error("BeatenByThread got interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    private Callable<Void> createSetBeatenByTask(LeaderboardEntry targetEntry, List<LeaderboardEntry> allEntries) {
        return () -> {
            List<LeaderboardEntryMinimal> beatenByEntries = allEntries.stream()
                    .filter(lbe -> lbe.getScore() < targetEntry.getScore())
                    .filter(lbe -> lbe.getCreatedAt().isAfter(targetEntry.getCreatedAt()))
                    .filter(lbe -> Boolean.FALSE.equals(lbe.getIsInvalidRun()))
                    .sorted(Comparator.comparing(LeaderboardEntry::getCreatedAt).reversed())
                    .limit(5)
                    .map(lbe -> modelMapper.map(lbe, LeaderboardEntryMinimal.class))
                    .collect(Collectors.toList());
            targetEntry.setBeatenBy(beatenByEntries);
            return null;
        };
    }

    @Transactional
    public void deleteLeaderboardEntries(Collection<LeaderboardEntry> leaderboardEntries) {
        leaderboardRepository.deleteAll(leaderboardEntries);
    }

    public List<OverallRankingView> getOverallRanking(Set<Integer> mapCategoryIds, int page, int limit) {
        int offset = (page - 1) * limit;
        return leaderboardRepository.getOverallRanking(mapCategoryIds, limit, offset);
    }

    @Cacheable(value = CACHE_OFFICIAL_LEADERBOARD_BY_PLAYERNAME, key = "{#playerName}")
    public List<LeaderboardByPlayerView> getOfficialTrackLeaderboardForPlayer(String playerName) {
        Sort sorting = Sort.by(
                Sort.Order.asc("track.mapName"),
                Sort.Order.asc("track.parentCategory"),
                Sort.Order.asc("track.name"),
                Sort.Order.desc("l.createdAt"),
                Sort.Order.desc("beatenBy.createdAt")
        );
        return leaderboardRepository.findByPlayerNameAndMapCategoryIdsIn(playerName, MapCategory.getOfficialCategoriesIds(), sorting);
    }

    @Cacheable(value = CACHE_COMMUNITY_LEADERBOARD_BY_PLAYERNAME_CURRENT_SEASON, key = "{#playerName}")
    public List<LeaderboardByPlayerView> getTrackLeaderboardForPlayerForCurrentSeason(String playerName) {
        Sort sorting = Sort.by(
                Sort.Order.asc("track.mapName"),
                Sort.Order.asc("track.parentCategory"),
                Sort.Order.asc("track.name"),
                Sort.Order.desc("l.createdAt"),
                Sort.Order.desc("beatenBy.createdAt")
        );
        List<Long> seasonTracks =
                communitySeasonService.getCurrentSeasonTrackIds();
        return leaderboardRepository.findByTrackIdInAndPlayerName(seasonTracks, playerName, sorting);
    }
}

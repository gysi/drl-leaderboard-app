package de.gregord.drlleaderboardbackend.services;

import de.gregord.drlleaderboardbackend.domain.CommunityRankingView;
import de.gregord.drlleaderboardbackend.domain.Season;
import de.gregord.drlleaderboardbackend.domain.TrackCommunitySeasonView;
import de.gregord.drlleaderboardbackend.repositories.CommunitySeasonsRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static de.gregord.drlleaderboardbackend.config.CacheConfig.CACHE_COMMUNITY_CURRENT_SEASON_TRACKIDS;
import static de.gregord.drlleaderboardbackend.config.CacheConfig.CACHE_OVERALLRANKING_COMMUNITY_CURRENT_SEASON;

@Service
@Transactional(readOnly = true)
public class CommunitySeasonService {

    private final CommunitySeasonsRepository communitySeasonsRepository;

    public CommunitySeasonService(CommunitySeasonsRepository communitySeasonsRepository) {
        this.communitySeasonsRepository = communitySeasonsRepository;
    }

    public List<TrackCommunitySeasonView> findBySeasonIdAndExcludedIsFalseAndSortedByDifficulty(int seasonId) {
        return communitySeasonsRepository.findBySeasonIdAndExcludedIsFalseAndSortedByDifficulty(seasonId);
    }

    @Cacheable(CACHE_COMMUNITY_CURRENT_SEASON_TRACKIDS)
    public List<Long> getCurrentSeasonTrackIds() {
        return communitySeasonsRepository.findTrackIdsBySeasonIdAndExcludedIsFalse(Season.getCurrentSeasonId());
    }

    // TODO ADD a better cachekey, look at the overallranking from the Leaderboardrepo
    @Cacheable(CACHE_OVERALLRANKING_COMMUNITY_CURRENT_SEASON)
    public List<CommunityRankingView> getOverallRankingCurrentSeason(int page, int limit) {
        int offset = (page - 1) * limit;
        return communitySeasonsRepository.getOverallRankingCurrentSeason(
                Season.getCurrentSeasonId(), Season.getCurrentSeason().getSeasonStartDate(), limit, offset);
    }
}

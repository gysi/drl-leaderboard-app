package de.gregord.drlleaderboardbackend.repositories;

import de.gregord.drlleaderboardbackend.domain.LeaderboardByPlayerView;
import de.gregord.drlleaderboardbackend.entities.Track;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TracksRepository extends JpaRepository<Track, Long> {
    Optional<Track> findByGuid(String guid);

    Collection<Track> findByGuidIn(Collection<String> guids);

    @Cacheable(value = "tracks", key = "#type")
    <T> List<T> findBy(Sort sort, Class<T> type);

    @Query(value = """
            SELECT
                t.id as id,
                t.name as name,
                t.map_name as mapName,
                t.parent_category as parentCategory
            FROM tracks t
                     LEFT JOIN leaderboards l ON t.id = l.track_id AND l.player_name = :playerName
            WHERE l.id IS NULL
            ORDER BY t.map_name, t.parent_category, t.name
            """, nativeQuery = true)
    List<LeaderboardByPlayerView.LeaderboardByPlayerView_Track> findMissingTracksByPlayerName(String playerName);

    Collection<Track> findByIdNotIn(Collection<Long> ids);
}

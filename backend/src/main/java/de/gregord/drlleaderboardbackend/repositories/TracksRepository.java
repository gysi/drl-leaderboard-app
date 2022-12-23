package de.gregord.drlleaderboardbackend.repositories;

import de.gregord.drlleaderboardbackend.entities.Track;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TracksRepository extends JpaRepository<Track, Long> {
    Optional<Track> findByGuid(String guid);
//    @EntityGraph(attributePaths = {"leaderboardEntryEntries"}, type = EntityGraph.EntityGraphType.LOAD)
//    TrackView findByGuid(String guid, Sort sort);

    //    @Query("""
//        SELECT
//            t.id as id,
//            t.name as name,
//            t.mapName as mapName,
//            t.parentCategory as parentCategory
//         FROM Track t
//    """)
    <T> List<T> findBy(Sort sort, Class<T> type);
}

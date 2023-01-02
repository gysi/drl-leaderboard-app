package de.gregord.drlleaderboardbackend.repositories;

import de.gregord.drlleaderboardbackend.entities.Track;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TracksRepository extends JpaRepository<Track, Long> {
    Optional<Track> findByGuid(String guid);

    @Cacheable(value = "tracks", key = "#type.name")
    <T> List<T> findBy(Sort sort, Class<T> type);
}

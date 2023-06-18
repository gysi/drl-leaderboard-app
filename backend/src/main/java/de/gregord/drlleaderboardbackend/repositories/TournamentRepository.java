package de.gregord.drlleaderboardbackend.repositories;

import de.gregord.drlleaderboardbackend.domain.TournamentView;
import de.gregord.drlleaderboardbackend.entities.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    Optional<Tournament> findTournamentByDrlId(String drlId);

    @Query("""
        SELECT t FROM Tournament t
            WHERE t.isTestTournament = false AND t.status = 'complete'
                AND t.registrationEndAt >= :lowerBound AND t.registrationEndAt < :upperBound
            ORDER BY t.registrationEndAt ASC
    """)
    Stream<Tournament> streamTournaments(LocalDateTime lowerBound, LocalDateTime upperBound);

    @Query("""
        SELECT
            t.guid as guid,
            t.title as title,
            t.customMapTitle as trackName,
            t.imageUrl as imgUrl,
            jsonb_path_query_array(t.rankings, '$[0 to 2].profileName') AS top3,
            t.registrationEndAt as startDate,
            t.status as status
         FROM Tournament t
         WHERE t.isTestTournament = false
             AND t.registrationEndAt >= :lowerBound AND t.registrationEndAt < :upperBound
         ORDER BY t.registrationEndAt DESC
    """)
    List<TournamentView> _getTournament(LocalDateTime lowerBound, LocalDateTime upperBound);
}

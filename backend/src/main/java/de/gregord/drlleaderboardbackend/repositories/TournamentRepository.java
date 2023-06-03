package de.gregord.drlleaderboardbackend.repositories;

import de.gregord.drlleaderboardbackend.entities.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
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
}

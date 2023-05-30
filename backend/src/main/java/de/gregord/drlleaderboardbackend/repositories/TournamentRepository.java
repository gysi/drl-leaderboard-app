package de.gregord.drlleaderboardbackend.repositories;

import de.gregord.drlleaderboardbackend.entities.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.stream.Stream;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    Optional<Tournament> findTournamentByDrlId(String drlId);

    @Query("""
        SELECT t FROM Tournament t
            WHERE t.isTestTournament = false and t.status = 'complete'
            ORDER BY t.createdAt ASC
    """)
    Stream<Tournament> streamTournamentsForOverallRanking();
}

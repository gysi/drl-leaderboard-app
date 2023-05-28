package de.gregord.drlleaderboardbackend.repositories;

import de.gregord.drlleaderboardbackend.entities.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
}

package de.gregord.drlleaderboardbackend.repositories;

import de.gregord.drlleaderboardbackend.entities.Player;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByPlayerIdIn(List<String> playerIds);

    @Query("""
            SELECT DISTINCT p.playerName
            FROM Player p
            WHERE p.playerName ilike :playerName
            """)
    List<String> findDistinctPlayerNames(String playerName, Pageable pageable);
}

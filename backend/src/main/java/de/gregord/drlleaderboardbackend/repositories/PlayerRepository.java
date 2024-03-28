package de.gregord.drlleaderboardbackend.repositories;

import de.gregord.drlleaderboardbackend.entities.Player;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    @Transactional(readOnly = true)
    Optional<Player> findByPlayerId(String playerId);

    List<Player> findByPlayerIdIn(List<String> playerIds);

    @Query("""
            SELECT DISTINCT p.playerName
            FROM Player p
            WHERE p.playerName ilike :playerName
            """)
    List<String> findDistinctPlayerNames(String playerName, Pageable pageable);
}

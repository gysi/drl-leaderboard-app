package de.gregord.drlleaderboardbackend.services;

import de.gregord.drlleaderboardbackend.entities.Player;
import de.gregord.drlleaderboardbackend.repositories.PlayerRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    private PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository){
        this.playerRepository = playerRepository;
    }

    public Map<String, Player> getPlayersByDrlIdAsMap(List<String> playerIds){
        return playerRepository.findByPlayerIdIn(playerIds)
                .stream()
                .collect(Collectors.toMap(Player::getPlayerId, Function.identity()));
    }

    public List<String> findPlayersByName(String playerName){
        return playerRepository.findDistinctPlayerNames(
                playerName.toLowerCase().trim() + '%',
                PageRequest.of(0, 50)
                        .withSort(Sort.by(Sort.Order.asc("playerName")))
        );
    }

    public void savePlayers(List<Player> playersToBeSaved) {
        playerRepository.saveAll(playersToBeSaved);
    }
}

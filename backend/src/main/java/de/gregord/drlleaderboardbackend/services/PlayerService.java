package de.gregord.drlleaderboardbackend.services;

import de.gregord.drlleaderboardbackend.entities.Player;
import de.gregord.drlleaderboardbackend.repositories.PlayerRepository;
import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static de.gregord.drlleaderboardbackend.config.CacheConfig.CACHE_INTERNAL_LAST_SAVED_PLAYERS;

@Service
public class PlayerService {
    private static final Logger LOG = LoggerFactory.getLogger(PlayerService.class);
    private final PlayerRepository playerRepository;
    private final CacheManager cacheManager;

    public PlayerService(PlayerRepository playerRepository,
                         CacheManager cacheManager) {
        this.playerRepository = playerRepository;
        this.cacheManager = cacheManager;
    }

    @Transactional(readOnly = true)
    public Map<String, Player> getPlayersByDrlIdAsMap(List<String> playerIds) {
        if (playerIds == null || playerIds.isEmpty()) {
            return new HashMap<>();
        }
        return playerRepository.findByPlayerIdIn(playerIds)
                .stream()
                .collect(Collectors.toMap(Player::getPlayerId, Function.identity()));
    }

    @Transactional(readOnly = true)
    public List<String> findPlayersByName(String playerName) {
        return playerRepository.findDistinctPlayerNames(
                playerName.toLowerCase().trim() + '%',
                PageRequest.of(0, 50)
                        .withSort(Sort.by(Sort.Order.asc("playerName")))
        );
    }

    public void savePlayers(List<Player> newPlayers, List<Player> updatedPlayers) {
        if(newPlayers.isEmpty() && updatedPlayers.isEmpty()){
            return;
        }
        LOG.debug("Updating/Saving Players...");
        Cache cache = cacheManager.getCache(CACHE_INTERNAL_LAST_SAVED_PLAYERS);
        boolean dataIntegrityViolationException = false;
        int retries = 0;
        do {
            if (dataIntegrityViolationException) {
                List<Player> removeFromNewPlayers = new ArrayList<>();
                int finalRetries = retries;
                newPlayers.stream().forEach(p -> {
                    Cache.ValueWrapper valueWrapper = cache.get(p.getPlayerId());
                    if (valueWrapper != null) {
                        Player cachedPlayer = (Player) valueWrapper.get();
                        if (cachedPlayer != null) {
                            p.setId(cachedPlayer.getId());
                            removeFromNewPlayers.add(p);
                            if (cachedPlayer.getUpdatedAt().isBefore(p.getUpdatedAt())) {
                                updatedPlayers.add(p);
                            }
                        }
                    } else {
                        if (finalRetries > 2) {
                            LOG.warn("Too many retries for getting a player from cache while saving, getting him manually");
                            Optional<Player> idByPlayerId = playerRepository.findByPlayerId(p.getPlayerId());
                            idByPlayerId.ifPresent(found -> {
                                p.setId(found.getId());
                                removeFromNewPlayers.add(p);
                                if (found.getUpdatedAt().isBefore(p.getUpdatedAt())) {
                                    updatedPlayers.add(p);
                                }
                            });
                        }
                    }
                });
                removeFromNewPlayers.forEach(newPlayers::remove);
                dataIntegrityViolationException = false;
            }
            try {
                playerRepository.saveAll(ListUtils.union(newPlayers, updatedPlayers));
                newPlayers.forEach(p -> {
                    cache.put(p.getPlayerId(), p);
                });
            } catch (DataIntegrityViolationException e) {
                LOG.warn("Error saving players on attempt " + (retries + 1), e);
                dataIntegrityViolationException = true;
                newPlayers.forEach(p -> p.setId(null)); // this is needed because the generator did already set ids
                retries++;
            }
        } while (dataIntegrityViolationException);
    }
}

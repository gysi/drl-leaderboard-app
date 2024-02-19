package de.gregord.drlleaderboardbackend.controllers;

import de.gregord.drlleaderboardbackend.services.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService){
        this.playerService = playerService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<String>> findPlayers(@RequestParam String playerName) {
        List<String> players = playerService.findPlayersByName(playerName);
        return ResponseEntity.ok(players);
    }
}

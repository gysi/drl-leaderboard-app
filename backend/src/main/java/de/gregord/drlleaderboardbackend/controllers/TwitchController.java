package de.gregord.drlleaderboardbackend.controllers;

import de.gregord.drlleaderboardbackend.domain.twitch.ActiveStreams;
import de.gregord.drlleaderboardbackend.services.twitch.TwitchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/twitch")
public class TwitchController {

    private final TwitchService twitchService;

    public TwitchController(TwitchService twitchService) {
        this.twitchService = twitchService;
    }

    @GetMapping("/streams")
    public List<ActiveStreams> streams() {
        return twitchService.getActiveStreams();
    }
}

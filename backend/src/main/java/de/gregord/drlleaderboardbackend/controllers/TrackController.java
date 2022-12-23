package de.gregord.drlleaderboardbackend.controllers;

import de.gregord.drlleaderboardbackend.domain.TrackView;
import de.gregord.drlleaderboardbackend.repositories.TracksRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/tracks")
public class TrackController {

    private final TracksRepository tracksRepository;
    private final ModelMapper modelMapper;

    public TrackController(
            ModelMapper modelMapper,
            TracksRepository tracksRepository
    ) {
        this.modelMapper = modelMapper;
        this.tracksRepository = tracksRepository;
    }

    @GetMapping
    public ResponseEntity<List<TrackView>> tracks() {
        List<TrackView> all = tracksRepository.findBy(
                Sort.by(Sort.Order.asc("mapName"), Sort.Order.asc("parentCategory"), Sort.Order.asc("name")),
                TrackView.class
        );
        return ResponseEntity.ok(all);
    }
}

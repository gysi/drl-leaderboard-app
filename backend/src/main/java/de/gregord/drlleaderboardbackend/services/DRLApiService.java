package de.gregord.drlleaderboardbackend.services;

import de.gregord.drlleaderboardbackend.entities.Track;
import de.gregord.drlleaderboardbackend.repositories.TracksRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DRLApiService {
    public static final Logger LOG = LoggerFactory.getLogger(DRLApiService.class);
    private String token;
    private String mapDetailEndpoint;
    private TracksRepository tracksRepository;

    public DRLApiService(
            @Value("${app.drl-api.token}") String token,
            @Value("${app.drl-api.map-detail-endpoint}") String mapDetailEndpoint,
            TracksRepository tracksRepository
    ) {
        this.token = token;
        this.mapDetailEndpoint = mapDetailEndpoint;
        this.tracksRepository = tracksRepository;
    }

    public LinkedHashMap getMapDetails(String trackId){
        Optional<Track> byId = tracksRepository.findById(Long.parseLong(trackId));
        if (byId.isEmpty()) {
            throw new RuntimeException("Could not find track with id " + trackId + ".");
        }
        String guid = byId.get().getGuid();
//        guid = "CMP-0c57443b36b17c5022130b5a"; // THE LOOP
//        guid = "CMP-1630238b227713aeed900973"; // Arround Arround
        UriComponentsBuilder mapsEndpointBuilder = UriComponentsBuilder.fromUriString(mapDetailEndpoint);
        String requestUrl = mapsEndpointBuilder.buildAndExpand(Map.of("token", token, "guid", guid)).toUriString();
        LOG.info("Requesting map details for guid " + guid + " from " + requestUrl + ".");
        Map<String, Object> response = new RestTemplate().getForObject(
                requestUrl,
                Map.class
        );
        if (!(boolean) response.get("success")) {
            throw new RuntimeException("Could not get map details for guid " + guid + ".");
        }

        return (LinkedHashMap) ((List) ((LinkedHashMap) response.get("data")).get("data")).get(0);
    }
}

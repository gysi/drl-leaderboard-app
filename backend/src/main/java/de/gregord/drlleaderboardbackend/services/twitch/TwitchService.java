package de.gregord.drlleaderboardbackend.services.twitch;

import com.github.twitch4j.ITwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.helix.domain.StreamList;
import com.github.twitch4j.helix.domain.UserList;
import de.gregord.drlleaderboardbackend.domain.twitch.ActiveStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static de.gregord.drlleaderboardbackend.config.CacheConfig.CACHE_TWITCH_STREAMS;

@Service
public class TwitchService {
    private final Logger LOG = LoggerFactory.getLogger(TwitchService.class);
    private final ITwitchClient twitchClient;

    private static final int STREAMS_LIMIT = 10;
    private static final String GAME_ID_DRL = "1252397026";
    private static final String GAME_ID_ENSHROUDED = "737987407";
    private static final String GAME_ID_HOTTUB = "116747788"; // just for the lols


    public TwitchService(
            @Value("${app.twitch.auth.client-id}") String clientId,
            @Value("${app.twitch.auth.client-secret}") String clientSecret
    ) {
        this.twitchClient = TwitchClientBuilder.builder()
                .withClientId(clientId)
                .withClientSecret(clientSecret)
                .withEnableHelix(true)
                .build();
    }

    @Cacheable(CACHE_TWITCH_STREAMS)
    public List<ActiveStreams> getActiveStreams() {
        try {
            StreamList list = twitchClient.getHelix().getStreams(
                    null,
                    null,
                    null,
                    STREAMS_LIMIT,
                    List.of(GAME_ID_DRL),
                    null,
                    null,
                    null).execute();
            List<ActiveStreams> activeStreamsList = new ArrayList<>(list.getStreams().stream().map(stream -> {
                ActiveStreams aStream = new ActiveStreams();
                aStream.setUserId(stream.getUserId());
                aStream.setUserName(stream.getUserName());
                aStream.setStreamThumbnail(stream.getThumbnailUrl());
                aStream.setViewerCount(stream.getViewerCount());
                return aStream;
            }).toList());
            if (activeStreamsList.isEmpty()) {
                return activeStreamsList;
            }

            List<String> userIds = activeStreamsList.stream().map(ActiveStreams::getUserId).toList();
            if (!userIds.isEmpty()) {
                UserList userList = twitchClient.getHelix().getUsers(null, userIds, null).execute();
                userList.getUsers().forEach(u -> {
                    for (ActiveStreams stream : activeStreamsList) {
                        if (stream.getUserId().equals(u.getId())) {
                            stream.setUserLogin(u.getLogin());
                            stream.setUserThumbnail(u.getProfileImageUrl());
                        }
                    }
                });
            }
            return activeStreamsList;
        } catch (Exception e ){
            LOG.error("Error while getting twitch streams.", e);
            return new ArrayList<>();
        }
    }
}

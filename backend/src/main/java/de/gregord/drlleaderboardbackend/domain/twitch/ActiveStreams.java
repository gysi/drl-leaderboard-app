package de.gregord.drlleaderboardbackend.domain.twitch;

import lombok.Data;

@Data
public class ActiveStreams {
    private String userId;
    private String userLogin;
    private String userName;
    private String userThumbnail;
    private String streamThumbnail;
    private Integer viewerCount;
}

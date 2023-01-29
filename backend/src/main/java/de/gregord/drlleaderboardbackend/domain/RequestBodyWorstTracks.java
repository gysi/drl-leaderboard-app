package de.gregord.drlleaderboardbackend.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class RequestBodyWorstTracks {
   @NotBlank(message = "playerName is mandatory")
   private String playerName;
   private List<@Pattern(regexp = "\\d+", message = "excludedTrack must be a number") String> excludedTracks;
}

package de.gregord.drlleaderboardbackend.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class RequestBodyWorstTracks {
   @NotBlank(message = "playerName is mandatory")
   private String playerName;
   private Boolean includeImprovementIsLongAgo = false;
   private Boolean includeWorstPosition = false;
   private Boolean includeMostBeatenByEntries = false;
   private Boolean includeFarthestBehindLeader = false;
   private Boolean includePotentiallyEasyToAdvance = false;
   private Boolean includeInvalidRuns = false;
   private Boolean includeNotCompleted = false;
   private List<@Pattern(regexp = "\\d+", message = "excludedTrack must be a number") String> excludedTracks;

}

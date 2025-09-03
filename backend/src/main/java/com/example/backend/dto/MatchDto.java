package com.example.backend.dto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.backend.entities.MatchStatus;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Builder
@Jacksonized
public class MatchDto 
{
    @Schema(description = "Match primary key")
    @JsonView({Views.Get.class, Views.Put.class})
    Long id;

    @Schema(description = "Tournament ID")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    Long tournamentId;

    @Schema(description = "Tournament name")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    String tournamentName;
    
    @Schema(description = "Match status")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    MatchStatus status;

    @Schema(description = "Match type")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    String type;

    @Schema(description = "Round number")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    Integer round;

    @Schema(description = "Table number")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    Integer tableNumber;

    @Schema(description = "Best of number")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    Integer bestOf;

    @Schema(description = "List of participant Keycloak IDs")
    @JsonView({Views.Get.class, Views.Post.class})
    private List<UUID> participantIds;

    @Schema(description = "List of participant usernames")
    @JsonView({Views.Get.class, Views.Post.class})
    private List<String> participantUsernames;

    @Schema(description = "Keycloak ID of the winner")
    @JsonView({Views.Get.class, Views.Put.class})
    private UUID winnerId;

    @Schema(description = "Username of the winner")
    @JsonView({Views.Get.class, Views.Post.class})
    private String winnerUsername;

    @Schema(description = "List of match winners")
    @JsonView({Views.Get.class, Views.Put.class})
    List<String> gameWinners;

    @Schema(description = "Map of achievements for each participant")
    @JsonView({Views.Get.class, Views.Put.class})
    Map<String, Map<Long, Integer>> achievements;
}

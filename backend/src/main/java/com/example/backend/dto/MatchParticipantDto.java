package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class MatchParticipantDto 
{
    @Schema(description = "Match Participant primary key")
    @JsonView({Views.Get.class, Views.Put.class})
    private Long matchId;

    @Schema(description = "Player ID")
    @JsonView({Views.Get.class, Views.Put.class})
    private Long playerId;

    @Schema(description = "Whether the participant is the winner of the match")
    @JsonView({Views.Get.class, Views.Put.class})
    private boolean isWinner;
}

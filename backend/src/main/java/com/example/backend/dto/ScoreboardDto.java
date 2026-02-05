package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ScoreboardDto 
{
    @Schema(description = "Tournament ID")
    @JsonView({Views.Get.class, Views.Post.class, Views.Put.class})
    private Long tournamentId;

    @Schema(description = "Player ID")
    @JsonView({Views.Get.class, Views.Post.class, Views.Put.class})
    private Long playerId;

    @Schema(description = "Total points for this participant")
    @JsonView(Views.Get.class)
    private float points;
}

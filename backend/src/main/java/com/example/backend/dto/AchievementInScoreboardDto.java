package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class AchievementInScoreboardDto 
{
    @Schema(description = "Tournament ID (part of scoreboard key)")
    @JsonView({Views.Get.class, Views.Post.class, Views.Put.class})
    Long tournamentId;

    @Schema(description = "Player ID (part of scoreboard key)")
    @JsonView({Views.Get.class, Views.Post.class, Views.Put.class})
    Long playerId;

    @Schema(description = "Achivement ID")
    @JsonView({Views.Get.class, Views.Post.class, Views.Put.class})
    private Long achievementId;

    @Schema(description = "Points earned for this achievement in the scoreboard")
    @JsonView({Views.Get.class, Views.Post.class, Views.Put.class})
    private float points;
}
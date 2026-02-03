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
    @Schema(description = "Achievement ID")
    @JsonView({Views.Get.class, Views.Post.class, Views.Put.class})
    private Long achievementId;

    @Schema(description = "Scoreboard ID (composite key: tournamentId + playerId)")
    @JsonView({Views.Get.class, Views.Post.class, Views.Put.class})
    private Long scoreboardId;

    @Schema(description = "Points earned for this achievement in the scoreboard")
    @JsonView({Views.Get.class, Views.Post.class, Views.Put.class})
    private Integer points;
}
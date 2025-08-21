package com.example.backend.dto;

import java.util.UUID;

import com.example.backend.entities.AchievementAggregationType;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Builder
@Jacksonized
public class AchievementDto 
{
    @Schema(description = "Achievement primary key")
    @JsonView({Views.Get.class, Views.Put.class})
    private Long id;

    @Schema(description = "ID of the tournament this achievement belongs to")
    @JsonView({Views.Get.class, Views.Post.class, Views.Put.class})
    private Long tournamentId;

    @Schema(description = "Name of the achievement", example = "The most amount of flying creatures")
    @JsonView({Views.Get.class, Views.Post.class, Views.Put.class})
    private String name;

    @Schema(description = "Prize for the achievement", example = "Booster Pack")
    @JsonView({Views.Get.class, Views.Post.class, Views.Put.class})
    private String price;

    @Schema(description = "How the achievement values should be aggregated (SUM, MAX, MIN)")
    @NotNull(groups = Views.Post.class)
    @JsonView({Views.Get.class, Views.Post.class, Views.Put.class})
    private AchievementAggregationType aggregationType;

    @Schema(description = "Keycloak ID (sub) of the winner")
    @JsonView({Views.Get.class, Views.Post.class, Views.Put.class}) 
    private UUID winnerId;

    @Schema(description = "Username of the winner")
    @JsonView({Views.Get.class, Views.Post.class, Views.Put.class})
    private String winnerUsername;
}

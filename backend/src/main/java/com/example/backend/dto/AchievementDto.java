package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class AchievementDto 
{
    @Schema(description = "Achievement primary key")
    @JsonView({Views.Get.class, Views.Put.class})
    private Long id;

    @Schema(description = "Name of the achievement", example = "The most amount of flying creatures")
    @JsonView({Views.Get.class, Views.Post.class, Views.Put.class})
    private String name;

    @Schema(description = "Prize for the achievement", example = "Booster Pack")
    @JsonView({Views.Get.class, Views.Post.class, Views.Put.class})
    private String priceDescription;

    @Schema(description = "Description of the achievement", example = "Awarded to the player with the most flying creatures in their deck")
    @JsonView({Views.Get.class, Views.Post.class, Views.Put.class})
    private String achievementDescription;

    @Schema(description = "How the achievement values should be aggregated (SUM, MAX, MIN)")
    @NotNull(groups = Views.Post.class)
    @JsonView({Views.Get.class, Views.Post.class, Views.Put.class})
    private Long achievementAggregationId;
}

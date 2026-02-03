package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class AchievementAggregationDto 
{
    @Schema(description = "Achievement aggregation primary key")
    @JsonView({Views.Get.class, Views.Put.class})
    private Long id;

    @Schema(description = "Aggregation type (e.g., SUM, AVERAGE)")
    @JsonView({Views.Get.class, Views.Put.class})
    private String aggregation;
}

package com.example.backend.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class MatchLogDto 
{
    @Schema(description = "Match log primary key")
    @JsonView({Views.Get.class, Views.Put.class})
    private Long matchId;

    @Schema(description = "Achievement ID")
    @JsonView({Views.Get.class, Views.Put.class})
    private Long achievementId;

    @Schema(description = "Count of the achievement logged")
    @JsonView({Views.Get.class, Views.Put.class})
    private int count;

    @Schema(description = "Timestamp when the log was created")
    @JsonView({Views.Get.class, Views.Put.class})
    private Instant loggedAt;

    @Schema(description = "Timestamp when the log was last updated")
    @JsonView({Views.Get.class, Views.Put.class})
    private Instant updatedAt;
}

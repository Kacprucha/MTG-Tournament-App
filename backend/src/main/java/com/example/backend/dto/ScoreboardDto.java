package com.example.backend.dto;

import java.util.Map;

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
public class ScoreboardDto 
{
    @Schema(description = "Scoreboard entry primary key")
    @JsonView(Views.Get.class)
    private Long id;

    @Schema(description = "Participant details")
    @JsonView(Views.Get.class)
    private ParticipantDto participant;

    @Schema(description = "Total points for this participant")
    @JsonView(Views.Get.class)
    private Integer points;
    
    @Schema(description = "Map of achievements for this participant (AchievementID -> Value)")
    @JsonView(Views.Get.class)
    private Map<String, Integer> achievements;
}

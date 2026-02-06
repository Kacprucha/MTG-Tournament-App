package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class AchivementInTournamentDto 
{
    @Schema(description = "ID of the achivement")
    @JsonView({Views.Get.class, Views.Post.class, Views.Put.class})
    private Long achivementId;

    @Schema(description = "ID of the tournament")
    @JsonView({Views.Get.class, Views.Post.class, Views.Put.class})
    private Long tournamentId;
}

package com.example.backend.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class TournamentDto 
{
    @Schema(description = "Tournament primary key")
    @JsonView({Views.Get.class, Views.Put.class})
    private Long id;

    @Schema(description = "Whether the tournament is a legacy tournament (created before the implementation of the tournament system)")
    @JsonView({Views.Get.class, Views.Put.class})
    private boolean isLegacy;

    @Schema(description = "Current status of the tournament (PENDING, PUBLISHED, IN_PROGRESS, FINISHED, CANCELLED)")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    private Long tournamentStatusId;

    @Schema(description = "Tournament name")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    private String name;

    @Schema(description = "Tournament type")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    private String type;

    @Schema(description = "Addon for the tournament")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    private String addon;

    @Schema(description = "Date of the tournament")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    private LocalDate date;
}

package com.example.backend.dto;

import java.time.LocalDate;
import java.util.List;

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
public class TournamentDto 
{
    @Schema(description = "Tournament primary key")
    @JsonView({Views.Get.class, Views.Put.class})
    private Long id;

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

    @Schema(description = "List of participant Keycloak IDs and usernames")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    private List<ParticipantDto> participants;

    @Schema(description = "List of matches in the tournament")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    private List<MatchDto> matches;

    @Schema(description = "List of scoreboards for the tournament")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    private List<ScoreboardDto> scoreboards;
}

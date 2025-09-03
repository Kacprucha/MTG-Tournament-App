package com.example.backend.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.example.backend.entities.TournamentStatus;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class TournamentDto 
{
    @Schema(description = "Tournament primary key")
    @JsonView({Views.Get.class, Views.Put.class})
    private Long id;

    @Schema(description = "Current status of the tournament (PENDING, PUBLISHED, IN_PROGRESS, FINISHED, CANCELLED)")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    private TournamentStatus status;

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

    @Schema(description = "List of participant Keycloak IDs")
    @JsonView({Views.Get.class, Views.Put.class})
    private List<UUID> participantIds;

    @Schema(description = "List of participant usernames")
    @JsonView({Views.Get.class, Views.Put.class})
    private List<String> participantUsernames;

    @Schema(description = "List of matches in the tournament")
    @JsonView({Views.Get.class, Views.Put.class})
    private List<MatchDto> matches;

    @Schema(description = "List of scoreboards for the tournament")
    @JsonView({Views.Get.class, Views.Put.class})
    private List<ScoreboardDto> scoreboard;

    @Schema(description = "List of achievements for the tournament")
    @JsonView({Views.Get.class, Views.Put.class})
    private List<AchievementDto> achievements;
}

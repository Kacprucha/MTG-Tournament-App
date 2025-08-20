package com.example.backend.dto;

import java.util.List;
import java.util.Map;

import com.example.backend.entities.MatchStatus;
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
public class MatchDto 
{
    @Schema(description = "Match primary key")
    @JsonView({Views.Get.class, Views.Put.class})
    Long id;

    @Schema(description = "Tournament ID")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    Long tournamentId;

    @Schema(description = "Tournament name")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    String tournamentName;
    
    @Schema(description = "Match status")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    MatchStatus status;

    @Schema(description = "Match type")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    String type;

    @Schema(description = "Round number")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    Integer round;

    @Schema(description = "Table number")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    Integer tableNumber;

    @Schema(description = "Best of number")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    Integer bestOf;

    @Schema(description = "List of participants")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    List<ParticipantDto> participants;

    @Schema(description = "Winer of the match")
    @JsonView({Views.Get.class, Views.Put.class})
    ParticipantDto winner;

    @Schema(description = "List of match winners")
    @JsonView({Views.Get.class, Views.Put.class})
    List<String> gameWinners;

    @Schema(description = "Map of achievements for each participant")
    @JsonView({Views.Get.class, Views.Put.class})
    Map<String, Map<Long, Integer>> achievements;
}

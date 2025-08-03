package com.example.backend.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class MatchDto 
{
    @Schema(description = "Match primary key")
    @JsonView({Views.Get.class, Views.Put.class})
    Long id;

    @Schema(description = "Tournament name")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    String tournamentName;

    @Schema(description = "First player")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    String player1;

    @Schema(description = "Second player")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    String player2;

    @Schema(description = "Table number")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    Integer tableNumber;

    @Schema(description = "Winer of the match")
    @JsonView({Views.Get.class, Views.Put.class})
    String winner;

    @Schema(description = "Player 1 HP")
    @JsonView({Views.Get.class, Views.Put.class})
    Integer player1Hp;

    @Schema(description = "Time of the match in seconds")
    @JsonView({Views.Get.class, Views.Put.class})
    Integer time;

    @Schema(description = "List of player 1 achievements")
    @JsonView({Views.Get.class, Views.Put.class})
    List<Integer> player1Achievements;
}

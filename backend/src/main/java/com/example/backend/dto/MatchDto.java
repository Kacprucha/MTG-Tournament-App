package com.example.backend.dto;

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

    @Schema(description = "Tournament ID")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    Long tournamentId;

    @Schema(description = "Match status")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    Long matchStatusId;

    @Schema(description = "Match type")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    String type;

    @Schema(description = "Round number")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    Integer round;

    @Schema(description = "Best of number")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    Integer bestOf;

    @Schema(description = "Table number")
    @JsonView({Views.Get.class, Views.Put.class, Views.Post.class})
    Integer tableNumber;
}

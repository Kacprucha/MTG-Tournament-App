package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class MatchStatusDto 
{
    @Schema(description = "Match Status primary key")
    @JsonView({Views.Get.class, Views.Put.class})
    private Long id;

    @Schema(description = "Status of the match (e.g., SCHEDULED, ONGOING, COMPLETED, CANCELLED)")
    @JsonView({Views.Get.class, Views.Put.class})
    private String status;
}

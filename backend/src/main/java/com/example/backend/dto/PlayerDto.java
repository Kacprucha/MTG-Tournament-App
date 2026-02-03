package com.example.backend.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class PlayerDto 
{
    @Schema(description = "Player primary key")
    @JsonView({Views.Get.class, Views.Put.class})
    private Long id;

    @Schema(description="UUID of the player form Keycloak")
    @JsonView({Views.Get.class, Views.Post.class, Views.Put.class})
    private UUID uuid;
}

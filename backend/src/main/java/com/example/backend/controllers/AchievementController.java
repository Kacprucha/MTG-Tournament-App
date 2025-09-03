package com.example.backend.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.AchievementDto;
import com.example.backend.dto.Views;
import com.example.backend.services.AchievementService;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("achievements")
@RequiredArgsConstructor
@Tag(name = "Achievements", description = "Endpoints for managing achievements")
public class AchievementController 
{
    private final AchievementService achievementService;

    @Operation(summary = "Create a new achievement for a tournament", description = "Defines a new achievement to be tracked in a tournament. Requires ADMIN role.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Achievement created", content = @Content(schema = @Schema(implementation = AchievementDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "403", description = "Forbidden, requires ADMIN role"),
        @ApiResponse(responseCode = "404", description = "Associated tournament not found")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(Views.Get.class)
    public ResponseEntity<AchievementDto> createAchievement(
            @RequestBody @Validated(Views.Post.class) @JsonView(Views.Post.class) AchievementDto createDto
    ) 
    {
        log.info(createDto.getName() + " | " + createDto.getPrice() + " | " + createDto.getAggregationType().toString() + " | " + createDto.getTournamentId());
        AchievementDto createdAchievement = achievementService.createAchievement(createDto);
        return new ResponseEntity<>(createdAchievement, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a list of all achievements for a tournament", description = "Retrieves all defined achievements for a specific tournament ID.")
    @ApiResponse(responseCode = "200", description = "List of achievements retrieved")
    @GetMapping(params = "tournamentId") // Endpoint: GET /api/achievements?tournamentId=1
    @JsonView(Views.Get.class)
    public ResponseEntity<List<AchievementDto>> getAchievementsByTournament(@RequestParam Long tournamentId) 
    {
        List<AchievementDto> achievements = achievementService.findAchievementsByTournament(tournamentId);
        return ResponseEntity.ok(achievements);
    }

    @Operation(summary = "Assign a winner to an achievement", description = "Sets the winner for a specific achievement after the tournament ends. Requires ADMIN role.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Winner assigned successfully", content = @Content(schema = @Schema(implementation = AchievementDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid winner ID"),
        @ApiResponse(responseCode = "403", description = "Forbidden, requires ADMIN role"),
        @ApiResponse(responseCode = "404", description = "Achievement not found")
    })
    @PutMapping("/{id}/winner")
    @JsonView(Views.Get.class)
    public ResponseEntity<AchievementDto> assignWinner(
            @PathVariable Long id,
            @RequestBody WinnerRequest winnerRequest // Proste DTO do przekazania ID zwycięzcy
    ) 
    {
        AchievementDto updatedAchievement = achievementService.assignWinner(id, winnerRequest.getWinnerId());
        return ResponseEntity.ok(updatedAchievement);
    }
    
    @Operation(summary = "Delete an achievement", description = "Removes an achievement definition from a tournament. Requires ADMIN role.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Achievement deleted successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden, requires ADMIN role"),
        @ApiResponse(responseCode = "404", description = "Achievement not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAchievement(@PathVariable Long id) 
    {
        achievementService.deleteAchievement(id);
        return ResponseEntity.noContent().build();
    }

    // Proste DTO pomocnicze
    @Getter @Setter
    private static class WinnerRequest 
    {
        private UUID winnerId;
    }
}

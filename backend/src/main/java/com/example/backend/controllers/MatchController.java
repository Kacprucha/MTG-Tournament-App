package com.example.backend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.MatchDto;
import com.example.backend.dto.Views;
import com.example.backend.services.MatchService;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "matches")
@RequiredArgsConstructor
@Tag(name = "Matches", description = "Endpoints for managing matches")
public class MatchController 
{
    private final MatchService matchService;

    @Operation(summary = "Create a new match for a tournament", description = "Generates a new match, usually as part of a new round. Requires ADMIN role.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Match created successfully", content = @Content(schema = @Schema(implementation = MatchDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "403", description = "Forbidden, requires ADMIN role"),
        @ApiResponse(responseCode = "404", description = "Associated tournament not found")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(Views.Get.class)
    public ResponseEntity<MatchDto> createMatch(
            @RequestBody @Validated(Views.Post.class) @JsonView(Views.Post.class) MatchDto createDto
    ) 
    {
        MatchDto createdMatch = matchService.createMatch(createDto);
        return new ResponseEntity<>(createdMatch, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a list of all matches for a tournament", description = "Retrieves a summary view of all matches for a specific tournament ID.")
    @ApiResponse(responseCode = "200", description = "List of matches retrieved")
    @GetMapping(params = "tournamentId") // Endpoint: GET /api/matches?tournamentId=1
    @JsonView(Views.Get.class) 
    public ResponseEntity<List<MatchDto>> getMatchesByTournament(@RequestParam Long tournamentId) 
    {
        List<MatchDto> matches = matchService.findMatchesByTournament(tournamentId);
        return ResponseEntity.ok(matches);
    }

    @Operation(summary = "Get a single match by ID", description = "Returns full details for a specific match.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Match details retrieved", content = @Content(schema = @Schema(implementation = MatchDto.class))),
        @ApiResponse(responseCode = "404", description = "Match not found")
    })
    @GetMapping("/{id}")
    @JsonView(Views.Get.class)
    public ResponseEntity<MatchDto> getMatchById(@PathVariable Long id) 
    {
        MatchDto match = matchService.getMatchById(id);
        return ResponseEntity.ok(match);
    }

    @Operation(summary = "Update match results", description = "Updates the results of a match, including game winners, achievements, and final status. Requires ADMIN role.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Match results updated successfully", content = @Content(schema = @Schema(implementation = MatchDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "403", description = "Forbidden, requires ADMIN role"),
        @ApiResponse(responseCode = "404", description = "Match not found")
    })
    @PutMapping("/{id}/results") 
    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(Views.Get.class)
    public ResponseEntity<MatchDto> updateMatchResults(
            @PathVariable Long id,
            @RequestBody @Validated(Views.Put.class) @JsonView(Views.Put.class) MatchDto updateDto
    ) 
    {
        MatchDto updatedMatch = matchService.updateMatchResults(id, updateDto);
        return ResponseEntity.ok(updatedMatch);
    }
}
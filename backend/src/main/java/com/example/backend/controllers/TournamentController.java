package com.example.backend.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.ParticipantDto;
import com.example.backend.dto.TournamentDto;
import com.example.backend.dto.Views;
import com.example.backend.services.MatchService;
import com.example.backend.services.TournamentService;
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
@RequestMapping(value = "tournaments")
@RequiredArgsConstructor
@Tag(name = "Tournaments", description = "Operations related to tournaments")
public class TournamentController 
{
    private final TournamentService tournamentService;
    private final MatchService matchService;

    @Operation(summary = "Create a new tournament", description = "Creates a new tournament with participants. Requires ADMIN role.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Tournament created successfully", content = @Content(schema = @Schema(implementation = TournamentDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "403", description = "Forbidden, requires ADMIN role"),
        @ApiResponse(responseCode = "409", description = "Tournament with this name already exists")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TournamentDto> createTournament(
            @RequestBody @Validated(Views.Post.class) @JsonView(Views.Post.class) TournamentDto createDto
    ) 
    {
        log.info("Received DTO name: {}", createDto.getName());
        TournamentDto createdTournament = tournamentService.createTournament(createDto);
        return new ResponseEntity<>(createdTournament, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a list of all tournaments", description = "Returns a summary view of all available tournaments.")
    @ApiResponse(responseCode = "200", description = "List of tournaments retrieved")
    @GetMapping
    @JsonView(Views.Get.class) 
    public ResponseEntity<List<TournamentDto>> getAllTournaments() 
    {
        List<TournamentDto> tournaments = tournamentService.getAllTournaments();
        return ResponseEntity.ok(tournaments);
    }

    @Operation(summary = "Get a single tournament by ID", description = "Returns all details for a specific tournament, including matches and scoreboard.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tournament details retrieved", content = @Content(schema = @Schema(implementation = TournamentDto.class))),
        @ApiResponse(responseCode = "404", description = "Tournament not found")
    })
    @GetMapping("/{id}")
    @JsonView(Views.Get.class) 
    public ResponseEntity<TournamentDto> getTournamentById(@PathVariable Long id) 
    {
        TournamentDto tournament = tournamentService.getTournamentWithDetails(id);
        return ResponseEntity.ok(tournament);
    }

    @Operation(summary = "Update a tournament's basic information", description = "Updates basic fields like name, type, date. Requires ADMIN role.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tournament updated successfully", content = @Content(schema = @Schema(implementation = TournamentDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data or ID mismatch"),
        @ApiResponse(responseCode = "403", description = "Forbidden, requires ADMIN role"),
        @ApiResponse(responseCode = "404", description = "Tournament not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(Views.Get.class)
    public ResponseEntity<TournamentDto> updateTournament(
            @PathVariable Long id,
            @RequestBody @Validated(Views.Put.class) @JsonView(Views.Put.class) TournamentDto updateDto
    ) 
    {
        // Upewniamy się, że ID w ścieżce i w ciele są zgodne
        if (updateDto.getId() != null && !id.equals(updateDto.getId())) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path and body do not match");
        }
        TournamentDto updatedTournament = tournamentService.updateTournament(id, updateDto);
        return ResponseEntity.ok(updatedTournament);
    }

    @Operation(summary = "Delete a tournament", description = "Deletes a tournament and all its related matches, achievements, and scoreboard entries. Requires ADMIN role.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Tournament deleted successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden, requires ADMIN role"),
        @ApiResponse(responseCode = "404", description = "Tournament not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Long id) 
    {
        tournamentService.deleteTournament(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<TournamentDto> updateTournamentStatus (
        @PathVariable Long id, 
        @RequestBody TournamentDto request
    ) 
    {
        TournamentDto updatedTournament = tournamentService.updateStatus(id, request.getStatus());
        return ResponseEntity.ok(updatedTournament);
    }

    @Operation(summary = "Generate all matches for a tournament", description = "Generates all round-robin pairings ('everyone vs everyone') for a given tournament and starts it. Requires ADMIN role.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Matches generated successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden, requires ADMIN role"),
        @ApiResponse(responseCode = "404", description = "Tournament not found"),
        @ApiResponse(responseCode = "409", description = "Matches have already been generated for this tournament")
    })
    @PostMapping("/{id}/start")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> generateNewPairings(@PathVariable Long id) 
    {
        matchService.startTournamentAndGenerateMatches(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Join a tournament", description = "Allows the authenticated user to join a tournament.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully joined the tournament"),
        @ApiResponse(responseCode = "404", description = "Tournament not found"),
        @ApiResponse(responseCode = "409", description = "User has already joined or tournament has already started")
    })
    @PostMapping("/{id}/join")
    public ResponseEntity<Void> joinTournament(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt 
    )
    {
        UUID userId = UUID.fromString(jwt.getSubject());
        String username = jwt.getClaimAsString("preferred_username");
        
        tournamentService.addParticipant(id, userId, username);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all participants for a tournament")
    @GetMapping("/{id}/participants")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ParticipantDto>> getTournamentParticipants(@PathVariable Long id) 
    {
        return ResponseEntity.ok(tournamentService.getParticipants(id));
    }
}

package com.example.backend.controllers;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.MatchDto;
import com.example.backend.dto.Views;
import com.example.backend.services.MatchService;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "matches")
@RequiredArgsConstructor
public class MatchController 
{
    private final MatchService matchService;

    @Operation(
        summary = "Create a new match",
        description = "Creates a new match with the provided details.",
        tags = {"post"})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(value = Views.Get.class)
    public MatchDto create (@RequestBody @JsonView(value = Views.Post.class) MatchDto matchDto) 
    {
        log.debug("Creating match: {}", matchDto);
        return matchService.CrateMatch(matchDto);
    }

    @Operation(
        summary = "Update an existing match",
        description = "Updates the details of an existing match.",
        tags = {"put"})
    @PutMapping("/{id}")
    @JsonView(value = Views.Get.class)
    public MatchDto update (
        @Parameter(description = "ID of the match to update", example = "1") 
        @PathVariable Long id,
        @RequestBody @JsonView(value = Views.Put.class) MatchDto matchDto)
    {
        log.debug("Updating match with id {}: {}", id, matchDto);
        return matchService.UpdateMatch(matchDto);
    }

    @Operation(
        summary = "Delete a match",
        description = "Deletes the match with the specified ID.",
        tags = {"delete"})
    @DeleteMapping("/{id}")
    public void delete (
        @Parameter(description = "ID of the match to delete", example = "1") 
        @PathVariable Long id) 
    {
        log.debug("Deleting match with id {}", id);
        matchService.DeleteMatch(id);
    }

    @Operation(
        summary = "Find matches by tournament name",
        description = "Retrieves all matches associated with a specific tournament.",
        tags = {"get"})
    @ApiResponse(
        responseCode = "200",
        content = {
            @Content(schema = @Schema(implementation = MatchDto.class), mediaType = "application/json")})
    @ApiResponse(
        responseCode = "404",
        content= {@Content(schema = @Schema())})
    @ApiResponse(
        responseCode= "500",
        content= {@Content(schema = @Schema())})
    @GetMapping("/tournament/{tournamentName}")
    @JsonView(value = Views.Get.class)
    public Collection<MatchDto> findByTournament(
        @Parameter(description = "Name of the tournament", example = "Summer Cup") 
        @PathVariable String tournamentName) 
    {
        log.debug("Finding matches for tournament: {}", tournamentName);
        return matchService.FindMatchesByTournament(tournamentName);
    }
}
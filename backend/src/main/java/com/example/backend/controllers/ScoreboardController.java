package com.example.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.ScoreboardDto;
import com.example.backend.services.ScoreboardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/scoreboard")
@RequiredArgsConstructor
@Tag(name = "Scoreboard", description = "Endpoints for managing scoreboard entries")
public class ScoreboardController 
{
    private final ScoreboardService scoreboardService;

    @Operation(summary = "Update a scoreboard entry", description = "Updates points and achievements for a specific player in a tournament. Requires ADMIN role.")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ScoreboardDto> updateScoreboardEntry(
            @PathVariable Long id,
            @RequestBody ScoreboardDto updateDto
    ) 
    {
        ScoreboardDto updatedEntry = scoreboardService.updateScoreboardEntry(id, updateDto);
        return ResponseEntity.ok(updatedEntry);
    }
}

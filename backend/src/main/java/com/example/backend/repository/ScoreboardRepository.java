package com.example.backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entities.Scoreboard;

public interface ScoreboardRepository extends JpaRepository<Scoreboard, Long>
{
    List<Scoreboard> findByTournamentId(Long tournamentId);

    Optional<Scoreboard> findByTournamentIdAndUserKeycloakId(Long tournamentId, UUID userKeycloakId);
}

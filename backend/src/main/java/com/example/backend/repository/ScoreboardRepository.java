package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.embeddable.ScoreboardId;
import com.example.backend.entities.Scoreboard;

public interface ScoreboardRepository extends JpaRepository<Scoreboard, ScoreboardId>
{
    List<Scoreboard> findByTournamentId(Long tournamentId);

    Optional<Scoreboard> findByTournamentIdAndPlayerId(Long tournamentId, Long playerId);
}

package com.example.backend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entities.Match;

public interface MatchRepository extends JpaRepository<Match, Long> 
{
    List<Match> findByTournamentId(Long tournamentId);

    List<Match> findByParticipantIdsContaining(UUID participantId);
}

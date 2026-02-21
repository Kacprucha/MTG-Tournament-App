package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entities.Match;
import com.example.backend.entities.MatchParticipant;

public interface MatchRepository extends JpaRepository<Match, Long> 
{
    List<Match> findByTournamentId(Long tournamentId);

    List<MatchParticipant> findByMatchId(Long matchId);

    List<MatchParticipant> findByPlayerId(Long playerId);

    Optional<MatchParticipant> findByMatchIdAndPlayerId(Long matchId, Long playerId);

    Optional<MatchParticipant> findByMatchIdAndIsWinnerTrue(Long matchId);

    List<MatchParticipant> findByPlayerIdAndMatchTournamentId(Long playerId, Long tournamentId);
}

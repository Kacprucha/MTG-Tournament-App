package com.example.backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.backend.entities.Match;
import com.example.backend.entities.Scoreboard;
import com.example.backend.entities.Tournament;

public interface TournamentRepository extends JpaRepository<Tournament, Long>
{
    boolean existsByName(String name);

    Optional<Tournament> findByName(String name);
    
     @Query("SELECT DISTINCT t FROM Tournament t " +
           "LEFT JOIN FETCH t.participantsIds " +
           "LEFT JOIN FETCH t.participantsUsernames " +
           "LEFT JOIN FETCH t.scoreboard s " +
           "LEFT JOIN FETCH t.matches m " +
           "LEFT JOIN FETCH t.achievements a " +
           "WHERE t.id = :id")
    Optional<Tournament> findByIdWithDetails(Long id);
    
    List<Tournament> findByParticipantsIdsContaining(UUID participantId);

    @Query("SELECT DISTINCT s FROM Scoreboard s LEFT JOIN FETCH s.achievements WHERE s.tournament.id = :tournamentId")
    List<Scoreboard> findScoreboardWithAchievementsByTournamentId(Long tournamentId);

    @Query("SELECT DISTINCT m FROM Match m LEFT JOIN FETCH m.achievements WHERE m.tournament.id = :tournamentId")
    List<Match> findMatchesWithAchievementsByTournamentId(Long tournamentId);
}

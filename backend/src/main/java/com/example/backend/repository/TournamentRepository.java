package com.example.backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.backend.entities.Tournament;

public interface TournamentRepository extends JpaRepository<Tournament, Long>
{
    boolean existsByName(String name);

    Optional<Tournament> findByName(String name);
    
    @Query("SELECT t FROM Tournament t LEFT JOIN FETCH t.matches LEFT JOIN FETCH t.scoreboard LEFT JOIN FETCH t.achievements WHERE t.id = :id")
    Optional<Tournament> findByIdWithDetails(Long id);
    
    List<Tournament> findByParticipantsIdsContaining(UUID participantId);
}

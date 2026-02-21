package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.embeddable.MatchParticipantId;
import com.example.backend.entities.MatchParticipant;

public interface MatchParticipantRepository extends JpaRepository<MatchParticipant, MatchParticipantId>
{
    
}

package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entities.Achievement;

public interface AchievementRepository extends JpaRepository<Achievement, Long> 
{
    List<Achievement> findByTournamentId(Long tournamentId);
}
package com.example.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.embeddable.AchivementInScoreboardId;
import com.example.backend.entities.AchivementInScoreboard;

public interface AchivementInScoreboardRepository extends JpaRepository<AchivementInScoreboard, AchivementInScoreboardId>
{
    Optional<AchivementInScoreboard> findByScoreBoardPlayerIdAndAchivementId (Long playerId, Long achivementId);
}

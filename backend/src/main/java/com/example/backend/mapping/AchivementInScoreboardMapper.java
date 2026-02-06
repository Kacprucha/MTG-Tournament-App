package com.example.backend.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.backend.dto.AchievementInScoreboardDto;
import com.example.backend.entities.Achievement;
import com.example.backend.entities.AchivementInScoreboard;
import com.example.backend.entities.Player;
import com.example.backend.entities.Tournament;

@Mapper(componentModel = "spring")
public interface AchivementInScoreboardMapper
{
    @Mapping(source = "achievement.id", target = "achievementId")
    @Mapping(source = "scoreboard.tournament.id", target = "tournamentId")
    @Mapping(source = "scoreboard.player.id", target = "playerId")
    AchievementInScoreboardDto toDto(AchivementInScoreboard entity);

    @Mapping(source = "achievementId", target = "achievement")
    @Mapping(source = "tournamentId", target = "scoreboard.tournament")
    @Mapping(source = "playerId", target = "scoreboard.player")
    AchivementInScoreboard toEntity(AchievementInScoreboardDto dto);

    @Mapping(source = "achievementId", target = "achievement")
    @Mapping(source = "tournamentId", target = "scoreboard.tournament")
    @Mapping(source = "playerId", target = "scoreboard.player")
    void updateEntity(AchievementInScoreboardDto dto, @MappingTarget AchivementInScoreboard entity);

    default Achievement mapAchievement(Long achievementId) 
    {
        if (achievementId == null) return null;

        Achievement achievement = new Achievement();
        achievement.setId(achievementId);

        return achievement;
    }

    default Tournament mapTournament(Long tournamentId) 
    {
        if (tournamentId == null) return null;

        Tournament tournament = new Tournament();
        tournament.setId(tournamentId);

        return tournament;
    }

    default Player mapPlayer(Long playerId) 
    {
        if (playerId == null) return null;

        Player player = new Player();
        player.setId(playerId);
        
        return player;
    }
}

package com.example.backend.mapping;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.backend.dto.TournamentDto;
import com.example.backend.entities.Tournament;

// @Mapper(
//     componentModel = "spring",
//     uses = {
//         MatchMapper.class, 
//         ScoreboardMapper.class,
//         AchievementMapper.class
//     }
// )
public interface TournamentMapper 
{    
    TournamentDto toDto(Tournament tournament);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "participantsIds", ignore = true)
    @Mapping(target = "participantsUsernames", ignore = true)
    @Mapping(target = "matches", ignore = true)
    @Mapping(target = "scoreboard", ignore = true)
    @Mapping(target = "achievements", ignore = true)
    @Mapping(target = "legacy", ignore = true)
    void updateEntityFromDto(TournamentDto dto, @MappingTarget Tournament entity);
}

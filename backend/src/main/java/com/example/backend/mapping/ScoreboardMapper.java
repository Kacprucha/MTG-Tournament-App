package com.example.backend.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.backend.dto.ScoreboardDto;
import com.example.backend.entities.Scoreboard;

@Mapper(componentModel = "spring")
public interface ScoreboardMapper 
{
    @Mapping(target = "tournamentId", source = "id.tournament")  
    @Mapping(target = "playerId", source = "id.player")
    @Mapping(target = "points", source = "points")
    ScoreboardDto toDto(Scoreboard scoreboard);

    @Mapping(target = "id.tournament", source = "tournamentId")  
    @Mapping(target = "id.player", source = "playerId")
    @Mapping(target = "points", source = "points")
    Scoreboard toEntity(ScoreboardDto dto);

    @Mapping(target = "points", source = "points")
    void updateEntity(ScoreboardDto dto, @MappingTarget Scoreboard entity);
}

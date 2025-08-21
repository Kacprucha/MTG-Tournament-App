package com.example.backend.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.backend.dto.AchievementDto;
import com.example.backend.entities.Achievement;

@Mapper(componentModel = "spring")
public interface AchievementMapper 
{
    @Mapping(source = "tournament.id", target = "tournamentId")
    @Mapping(target = "winnerUsername", ignore = true)
    AchievementDto toDto(Achievement achievement);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tournament", ignore = true)
    @Mapping(target = "winnerKeycloakId", ignore = true)
    @Mapping(target = "winnerUsername", ignore = true)
    void updateEntityFromDto(AchievementDto dto, @MappingTarget Achievement entity);
}

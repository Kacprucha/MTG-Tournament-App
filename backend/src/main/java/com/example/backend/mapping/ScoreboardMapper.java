package com.example.backend.mapping;

import java.util.Map;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.example.backend.dto.ParticipantDto;
import com.example.backend.dto.ScoreboardDto;
import com.example.backend.entities.Scoreboard;

@Mapper(componentModel = "spring")
public interface ScoreboardMapper 
{
    @Mapping(source = "scoreboard", target = "participant", qualifiedByName = "toParticipantDto")
    @Mapping(source = "achievements", target = "achievements", qualifiedByName = "mapAchievements")
    ScoreboardDto toDto(Scoreboard scoreboard);

    // --- Metody Pomocnicze ---

    @Named("toParticipantDto")
    default ParticipantDto participantFromScoreboard(Scoreboard scoreboard) {
        if (scoreboard == null) 
        {
            return null;
        }
        
        return new ParticipantDto(scoreboard.getUserKeycloakId(), scoreboard.getUsername());
    }

    @Named("mapAchievements")
    default Map<String, Integer> mapAchievements(Map<Long, Integer> achievements) 
    {
        if (achievements == null) 
        {
            return null;
        }

        return achievements.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toString(), // Klucz: Long as String
                        Map.Entry::getValue // Wartość: Integer
                ));
    }
}

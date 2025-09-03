package com.example.backend.mapping;

import java.util.Map;
import java.util.stream.Collectors;

import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.example.backend.dto.ScoreboardDto;
import com.example.backend.entities.Scoreboard;

//@Mapper(componentModel = "spring")
public interface ScoreboardMapper 
{
    @Mapping(source = "achievements", target = "achievements", qualifiedByName = "mapAchievements")
    ScoreboardDto toDto(Scoreboard scoreboard);

    // --- Metody Pomocnicze ---

    @Named("mapAchievements")
    default Map<String, Integer> mapAchievements(Map<Long, Integer> achievements) 
    {
        if (achievements == null) {
            return null; 
        }
        
        return achievements.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toString(),
                        Map.Entry::getValue
                ));
    }
}

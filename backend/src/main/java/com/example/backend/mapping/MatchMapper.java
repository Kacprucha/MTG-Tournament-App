package com.example.backend.mapping;

import java.util.Map;
import java.util.stream.Collectors;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.backend.dto.MatchDto;
import com.example.backend.entities.Match;
import com.example.backend.entities.MatchAchievement;

//@Mapper(componentModel = "spring")
public interface MatchMapper 
{
    @Mapping(source = "tournament.id", target = "tournamentId")
    @Mapping(source = "tournament.name", target = "tournamentName")
    @Mapping(target = "achievements", ignore = true)
    MatchDto toDto(Match match);

    @AfterMapping
    default void afterToDto(Match match, @MappingTarget MatchDto dto) 
    {
        // Logika transformacji List<MatchAchievement> w zagnieżdżoną mapę
        if (match.getAchievements() != null) {
            Map<String, Map<Long, Integer>> achievementsByParticipant = match.getAchievements().stream()
                .collect(Collectors.groupingBy(
                    ach -> ach.getParticipantId().toString(),
                    Collectors.toMap(
                        ach -> ach.getAchievement().getId(),
                        MatchAchievement::getValue
                    )
                ));
            dto.setAchievements(achievementsByParticipant);
        }
    }
}
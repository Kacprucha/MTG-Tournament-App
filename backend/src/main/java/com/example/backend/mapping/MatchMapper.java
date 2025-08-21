package com.example.backend.mapping;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.backend.dto.MatchDto;
import com.example.backend.dto.ParticipantDto;
import com.example.backend.entities.Match;
import com.example.backend.entities.MatchAchievement;

@Mapper(componentModel = "spring")
public interface MatchMapper 
{
    @Mapping(source = "tournament.id", target = "tournamentId")
    @Mapping(source = "tournament.name", target = "tournamentName")
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "winner", ignore = true)
    @Mapping(target = "achievements", ignore = true)
    MatchDto toDto(Match match);

    @AfterMapping
    default void afterToDto(Match match, @MappingTarget MatchDto dto) 
    {
        // Logika składania listy ParticipantDto
        if (match.getParticipantIds() != null && match.getParticipantUsernames() != null) 
        {
            dto.setParticipants(
                IntStream.range(0, match.getParticipantIds().size())
                    .mapToObj(i -> new ParticipantDto(
                        match.getParticipantIds().get(i), 
                        match.getParticipantUsernames().get(i)
                    ))
                    .collect(Collectors.toList())
            );
        }

        // Logika składania obiektu Winner
        if (match.getWinnerId() != null && match.getWinnerUsername() != null) 
        {
            dto.setWinner(new ParticipantDto(match.getWinnerId(), match.getWinnerUsername()));
        }
        
        // Logika transformacji mapy osiągnięć
        if (match.getAchievements() != null) 
        {
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
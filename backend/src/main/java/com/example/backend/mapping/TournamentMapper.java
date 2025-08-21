package com.example.backend.mapping;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.backend.dto.ParticipantDto;
import com.example.backend.dto.TournamentDto;
import com.example.backend.entities.Tournament;

@Mapper(
    componentModel = "spring",
    uses = {
        MatchMapper.class, 
        ScoreboardMapper.class,
        AchievementMapper.class
    }
)
public interface TournamentMapper 
{    
    @Mapping(target = "participants", ignore = true)
    TournamentDto toDto(Tournament tournament);

    @AfterMapping
    default void afterToDto(Tournament tournament, @MappingTarget TournamentDto dto) 
    {
        if (tournament.getParticipantsIds() != null && tournament.getParticipantsUsernames() != null) 
        {
            dto.setParticipants(
                IntStream.range(0, tournament.getParticipantsIds().size())
                    .mapToObj(i -> new ParticipantDto(
                        tournament.getParticipantsIds().get(i), 
                        tournament.getParticipantsUsernames().get(i)
                    ))
                    .collect(Collectors.toList())
            );
        }
    }
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "participantsIds", ignore = true) 
    @Mapping(target = "participantsUsernames", ignore = true)
    @Mapping(target = "matches", ignore = true)
    @Mapping(target = "scoreboard", ignore = true)
    @Mapping(target = "achievements", ignore = true)
    void updateEntityFromDto(TournamentDto dto, @MappingTarget Tournament entity);
}

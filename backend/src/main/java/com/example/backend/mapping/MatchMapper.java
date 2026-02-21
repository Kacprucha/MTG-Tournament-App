package com.example.backend.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.backend.dto.MatchDto;
import com.example.backend.entities.Match;
import com.example.backend.entities.Tournament;

@Mapper(componentModel = "spring")
public interface MatchMapper 
{
    @Mapping(source = "tournament.id", target = "tournamentId")
    MatchDto toDto(Match entity);

    @Mapping(source = "tournamentId", target = "tournament")
    Match toEntity(MatchDto dto);

    @Mapping(source = "tournamentId", target = "tournament")
    void updateEntity(MatchDto dto, @MappingTarget Match entity);

    default Tournament mapTournament(Long id) 
    {
        if (id == null) return null;

        Tournament tournament = new Tournament();
        tournament.setId(id);

        return tournament;
    }
}
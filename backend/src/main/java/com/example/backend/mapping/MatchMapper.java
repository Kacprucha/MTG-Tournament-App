package com.example.backend.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.backend.dto.MatchDto;
import com.example.backend.entities.Match;
import com.example.backend.entities.MatchStatus;
import com.example.backend.entities.Tournament;

@Mapper(componentModel = "spring")
public interface MatchMapper 
{
    @Mapping(source = "tournament.id", target = "tournamentId")
    @Mapping(source = "matchStatus.id", target = "matchStatusId")
    MatchDto toDto(Match entity);

    @Mapping(source = "tournamentId", target = "tournament")
    @Mapping(source = "matchStatusId", target = "matchStatus")
    Match toEntity(MatchDto dto);

    @Mapping(source = "tournamentId", target = "tournament")
    @Mapping(source = "matchStatusId", target = "matchStatus")
    void updateEntity(MatchDto dto, @MappingTarget Match entity);

    default Tournament mapTournament(Long id) 
    {
        if (id == null) return null;

        Tournament tournament = new Tournament();
        tournament.setId(id);

        return tournament;
    }

    default MatchStatus mapMatchStatus(Long id) 
    {
        if (id == null) return null;

        MatchStatus status = new MatchStatus();
        status.setId(id);
        
        return status;
    }
}
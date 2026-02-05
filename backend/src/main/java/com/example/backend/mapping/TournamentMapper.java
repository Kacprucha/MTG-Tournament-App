package com.example.backend.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.backend.dto.TournamentDto;
import com.example.backend.entities.Tournament;
import com.example.backend.entities.TournamentStatus;

@Mapper(componentModel = "spring")
public interface TournamentMapper 
{   
    @Mapping(source = "tournamentStatus.id", target = "tournamentStatusId")
    TournamentDto toDto(Tournament tournament);
    
    @Mapping(source = "tournamentStatusId", target = "tournamentStatus.id")
    Tournament toEntity(TournamentDto tournamentDto);

    @Mapping(source = "tournamentStatusId", target = "tournamentStatus.id")
    void updateEntityFromDto(TournamentDto tournamentDto, @MappingTarget Tournament tournament);

    default TournamentStatus mapTournamentStatus (Long id) 
    {
        if (id == null) return null;

        TournamentStatus status = new TournamentStatus();
        status.setId(id);
        
        return status;
    }
}

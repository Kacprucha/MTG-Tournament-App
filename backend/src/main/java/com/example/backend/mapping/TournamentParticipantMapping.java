package com.example.backend.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.backend.dto.TournamentParticipantDto;
import com.example.backend.entities.Player;
import com.example.backend.entities.Tournament;
import com.example.backend.entities.TournamentParticipant;

@Mapper(componentModel = "spring")
public interface TournamentParticipantMapping 
{
    @Mapping(source = "tournament.id", target = "tournamentId")
    @Mapping(source = "player.id", target = "playerId")
    TournamentParticipantDto toDto(TournamentParticipant tournamentParticipant);
    
    @Mapping(source = "tournamentId", target = "tournament")
    @Mapping(source = "playerId", target = "player")
    TournamentParticipant toEntity(TournamentParticipantDto tournamentParticipantDto);

    @Mapping(source = "tournamentId", target = "tournament")
    @Mapping(source = "playerId", target = "player")
    void updateEntity(TournamentParticipantDto dto, @MappingTarget TournamentParticipant entity);

    default Player mapPlayer (Long id) 
    {
      if (id == null) return null;

      Player player = new Player ();
      player.setId (id);

      return player;
    }

    default Tournament mapTournament (Long id) 
    {
      if (id == null) return null;

      Tournament tournament = new Tournament ();
      tournament.setId (id);

      return tournament;
    }
}

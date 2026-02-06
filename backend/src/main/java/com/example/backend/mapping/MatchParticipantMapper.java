package com.example.backend.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.backend.dto.MatchParticipantDto;
import com.example.backend.entities.Match;
import com.example.backend.entities.MatchParticipant;
import com.example.backend.entities.Player;

@Mapper(componentModel = "spring")
public interface MatchParticipantMapper 
{
    @Mapping(source = "match.id", target = "matchId")
    @Mapping(source = "player.id", target = "playerId")
    MatchParticipantDto toDto (MatchParticipant entity);

    @Mapping(source = "matchId", target = "match")
    @Mapping(source = "playerId", target = "player")
    MatchParticipant toEntity (MatchParticipantDto dto);

    @Mapping(source = "matchId", target = "match")
    @Mapping(source = "playerId", target = "player")
    void updateEntity (MatchParticipantDto dto, @MappingTarget MatchParticipant entity);

    default Match mapMatch (Long id) 
    {
        if (id == null) return null;

        Match match = new Match();
        match.setId(id);
        
        return match;
    }

    default Player mapPlayer (Long id) 
    {
        if (id == null) return null;

        Player player = new Player();
        player.setId(id);
        
        return player;
    }
}

package com.example.backend.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.backend.dto.AchivementInTournamentDto;
import com.example.backend.entities.Achievement;
import com.example.backend.entities.AchivementInTournament;
import com.example.backend.entities.Tournament;

@Mapper(componentModel = "spring")
public interface AchivementInTournamentMapper 
{
    @Mapping(source = "achievement.id", target = "achivementId")
    @Mapping(source = "tournament.id", target = "tournamentId")
    AchivementInTournamentDto toDto (AchivementInTournament entity);

    @Mapping(source = "achivementId", target = "achievement")
    @Mapping(source = "tournamentId", target = "tournament")
    AchivementInTournament toEntity (AchivementInTournamentDto dto);

    @Mapping(source = "achivementId", target = "achievement")
    @Mapping(source = "tournamentId", target = "tournament")
    void updateEntity(AchivementInTournamentDto dto, @MappingTarget AchivementInTournament entity);

    default Achievement mapAchivement (Long id) 
    {
        if (id == null) return null;

        Achievement achievement = new Achievement ();
        achievement.setId (id);

        return achievement;
    }

    default Tournament mapTournament (Long id) 
    {
        if (id == null) return null;

        Tournament tournament = new Tournament ();
        tournament.setId (id);

        return tournament;
    }
}

package com.example.backend.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.backend.dto.MatchDto;
import com.example.backend.dto.ParticipantDto;
import com.example.backend.entities.Match;
import com.example.backend.entities.Tournament;
import com.example.backend.repository.TournamentRepository;

@Mapper(componentModel = "spring")
public abstract class MatchMapper 
{
    @Autowired
    private TournamentRepository tournamentRepository;
    
    @Mappings({
        @Mapping(source = "tournament.id", target = "tournamentId"),
        @Mapping(source = "tournament.name", target = "tournamentName"),
        // Celowo ignorujemy te pola, bo obsłużymy je w metodzie z @AfterMapping
        @Mapping(target = "participants", ignore = true),
        @Mapping(target = "winner", ignore = true)
    })
    public abstract MatchDto toDto(Match match);

    @Mappings({
        @Mapping(source = "tournamentId", target = "tournament", qualifiedByName = "tournamentFromId"),
        // Ignorujemy pola, które nie mają bezpośredniego odpowiednika lub są tylko do odczytu
        @Mapping(target = "participantIds", ignore = true),
        @Mapping(target = "participantUsernames", ignore = true),
        @Mapping(target = "winnerId", ignore = true),
        @Mapping(target = "winnerUsername", ignore = true)
    })
    public abstract Match toEntity(MatchDto matchDto);

    @Named("tournamentFromId")
    protected Tournament tournamentFromId(Long tournamentId) {
        if (tournamentId == null) {
            return null;
        }
        return tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found with id: " + tournamentId));
    }

    @AfterMapping
    protected void afterToDto(Match match, @MappingTarget MatchDto dto) {
        // Logika do stworzenia listy ParticipantDto
        if (match.getParticipantIds() != null && match.getParticipantUsernames() != null) {
            List<ParticipantDto> participants = IntStream.range(0, match.getParticipantIds().size())
                    .mapToObj(i -> new ParticipantDto(match.getParticipantIds().get(i), match.getParticipantUsernames().get(i)))
                    .collect(Collectors.toList());
            dto.setParticipants(participants);
        }

        // Logika do stworzenia obiektu ParticipantDto dla zwycięzcy
        if (match.getWinnerId() != null && match.getWinnerUsername() != null) {
            ParticipantDto winner = new ParticipantDto(match.getWinnerId(), match.getWinnerUsername());
            dto.setWinner(winner);
        }
    }

    @AfterMapping
    protected void afterToEntity(MatchDto dto, @MappingTarget Match match) {
        // Logika do "rozbicia" listy ParticipantDto na dwie listy w encji
        if (dto.getParticipants() != null) {
            List<UUID> ids = new ArrayList<>();
            List<String> usernames = new ArrayList<>();
            for (ParticipantDto p : dto.getParticipants()) {
                ids.add(p.getKeycloakId());
                usernames.add(p.getUsername());
            }
            match.setParticipantIds(ids);
            match.setParticipantUsernames(usernames);
        }

        // Logika do "rozbicia" obiektu ParticipantDto dla zwycięzcy
        if (dto.getWinner() != null) {
            match.setWinnerId(dto.getWinner().getKeycloakId());
            match.setWinnerUsername(dto.getWinner().getUsername());
        }
    }
}
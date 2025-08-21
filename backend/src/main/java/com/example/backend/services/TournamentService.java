package com.example.backend.services;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.ParticipantDto;
import com.example.backend.dto.TournamentDto;
import com.example.backend.entities.Scoreboard;
import com.example.backend.entities.Tournament;
import com.example.backend.mapping.TournamentMapper;
import com.example.backend.repository.TournamentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TournamentService 
{
    private final TournamentRepository tournamentRepository;
    private final TournamentMapper tournamentMapper;
    
    public TournamentDto createTournament(TournamentDto createDto) 
    {
        if (tournamentRepository.existsByName(createDto.getName())) 
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tournament with this name already exists.");
        }

        Tournament tournament = new Tournament();
        tournament.setName(createDto.getName());
        tournament.setType(createDto.getType());
        tournament.setAddon(createDto.getAddon());
        tournament.setDate(createDto.getDate());
        
        if (createDto.getParticipants() != null && !createDto.getParticipants().isEmpty()) 
        {
            List<UUID> ids = createDto.getParticipants().stream().map(ParticipantDto::getKeycloakId).collect(Collectors.toList());
            List<String> usernames = createDto.getParticipants().stream().map(ParticipantDto::getUsername).collect(Collectors.toList());
            tournament.setParticipantsIds(ids);
            tournament.setParticipantsUsernames(usernames);
        }

        Tournament savedTournament = tournamentRepository.save(tournament);
        
        initializeScoreboard(savedTournament);
        
        savedTournament = tournamentRepository.save(savedTournament);
        
        return tournamentMapper.toDto(savedTournament);
    }

    public TournamentDto getTournamentWithDetails(Long id) 
    {
        return tournamentRepository.findByIdWithDetails(id)
                .map(tournamentMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament not found"));
    }

    public List<TournamentDto> getAllTournaments() 
    {
        return tournamentRepository.findAll().stream()
                .map(tournamentMapper::toDto) 
                .collect(Collectors.toList());
    }

    public TournamentDto updateTournament(Long id, TournamentDto updateDto) 
    {
        Tournament existingTournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament not found with id: " + id));

        if (updateDto.getName() != null) 
        {
            Optional<Tournament> tournamentWithSameName = tournamentRepository.findByName(updateDto.getName());
            if (tournamentWithSameName.isPresent() && !tournamentWithSameName.get().getId().equals(id)) 
            {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Tournament name '" + updateDto.getName() + "' is already taken.");
            }
        }

        tournamentMapper.updateEntityFromDto(updateDto, existingTournament);

        Tournament updatedTournament = tournamentRepository.save(existingTournament);
        
        return tournamentMapper.toDto(updatedTournament);
    }

     public void deleteTournament(Long id) 
     {
        if (!tournamentRepository.existsById(id)) 
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament not found with id: " + id);
        }
        
        tournamentRepository.deleteById(id);
    }

    // Metody Pomocnicze 
    
    private void initializeScoreboard(Tournament tournament) 
    {
        if (tournament.getParticipantsIds() == null) return;

        for (int i = 0; i < tournament.getParticipantsIds().size(); i++) 
        {
            Scoreboard entry = Scoreboard.builder()
                .userKeycloakId(tournament.getParticipantsIds().get(i))
                .username(tournament.getParticipantsUsernames().get(i))
                .points(0)
                .achievements(new HashMap<>())
                .build();
            tournament.addScoreboardEntry(entry); 
        }
    }
}

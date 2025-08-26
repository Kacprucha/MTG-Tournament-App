package com.example.backend.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.converters.DtoConverter;
import com.example.backend.dto.TournamentDto;
import com.example.backend.entities.Scoreboard;
import com.example.backend.entities.Tournament;
import com.example.backend.entities.TournamentStatus;
import com.example.backend.repository.TournamentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TournamentService 
{
    private final TournamentRepository tournamentRepository;
    private final DtoConverter dtoConverter;
    
    public TournamentDto createTournament(TournamentDto createDto) 
    {
        if (tournamentRepository.existsByName(createDto.getName())) 
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tournament with this name already exists.");
        }
        if (createDto.getParticipantIds().size() != createDto.getParticipantUsernames().size()) 
        {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Participant IDs and usernames lists must have the same size.");
        }

        Tournament tournament = Tournament.builder()
                .name(createDto.getName())
                .type(createDto.getType())
                .addon(createDto.getAddon())
                .date(createDto.getDate())
                .status(TournamentStatus.PENDING)
                .isLegacy(false) 
                .participantsIds(new HashSet<>(createDto.getParticipantIds()))
                .participantsUsernames(new HashSet<>(createDto.getParticipantUsernames()))
                .matches(new HashSet<>())
                .scoreboard(new HashSet<>())
                .achievements(new HashSet<>())
                .build();
        
        Tournament savedTournament = tournamentRepository.save(tournament);
        
        initializeScoreboard(tournament);
        
        savedTournament = tournamentRepository.save(savedTournament);

        return dtoConverter.toTournamentDto(savedTournament);
    }

    public TournamentDto getTournamentWithDetails(Long id) 
    {
        Tournament tournament = tournamentRepository.findByIdWithDetails(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament not found"));

        return dtoConverter.toTournamentDto(tournament);
    }

    public List<TournamentDto> getAllTournaments() 
    {
        return tournamentRepository.findAll().stream()
                .map(dtoConverter::toTournamentDto) 
                .collect(Collectors.toList());
    }

    public TournamentDto updateTournament(Long id, TournamentDto updateDto) 
    {
        Tournament existingTournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament not found with id: " + id));

        if (updateDto.getName() != null && !updateDto.getName().equals(existingTournament.getName())) {
            Optional<Tournament> tournamentWithSameName = tournamentRepository.findByName(updateDto.getName());
            if (tournamentWithSameName.isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Tournament name '" + updateDto.getName() + "' is already taken.");
            }

            existingTournament.setName(updateDto.getName());
        }
        
        Optional.ofNullable(updateDto.getType()).ifPresent(existingTournament::setType);
        Optional.ofNullable(updateDto.getAddon()).ifPresent(existingTournament::setAddon);
        Optional.ofNullable(updateDto.getDate()).ifPresent(existingTournament::setDate);
        Optional.ofNullable(updateDto.getStatus()).ifPresent(existingTournament::setStatus);
        
        Tournament updatedTournament = tournamentRepository.save(existingTournament);
        
        return dtoConverter.toTournamentDto(updatedTournament);
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
                .userKeycloakId(new ArrayList<>(tournament.getParticipantsIds()).get(i))
                .username(new ArrayList<>(tournament.getParticipantsUsernames()).get(i))
                .points(0f)
                .achievements(new HashMap<>())
                .build();
            tournament.addScoreboardEntry(entry); 
        }
    }
}

package com.example.backend.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.converters.DtoConverter;
import com.example.backend.dto.PlayerDto;
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

        Tournament tournament = new Tournament();
    
        tournament.setName(createDto.getName());
        tournament.setType(createDto.getType());
        tournament.setAddon(createDto.getAddon());
        tournament.setDate(createDto.getDate());
        tournament.setStatus(TournamentStatus.PENDING);
        tournament.setLegacy(false);
        tournament.setParticipantsIds(new ArrayList<>());
        tournament.setParticipantsUsernames(new ArrayList<>());
        
        if (createDto.getParticipantIds() != null && createDto.getParticipantUsernames() != null) 
        {
            if (createDto.getParticipantIds().size() != createDto.getParticipantUsernames().size()) 
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Participant IDs and usernames lists must have the same size.");
            }

            tournament.setParticipantsIds(createDto.getParticipantIds());
            tournament.setParticipantsUsernames(createDto.getParticipantUsernames());
        }
        
        Tournament savedTournament = tournamentRepository.save(tournament);
        
        initializeScoreboard(savedTournament);
        
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

        if (updateDto.getName() != null && !updateDto.getName().equals(existingTournament.getName())) 
        {
            Optional<Tournament> tournamentWithSameName = tournamentRepository.findByName(updateDto.getName());
            if (tournamentWithSameName.isPresent()) 
            {
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

    public TournamentDto updateStatus(Long id, TournamentStatus newStatus) 
    {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament not found with id: " + id));
        
        boolean changeLegal = false;

        switch (tournament.getStatus()) 
        {
            case PENDING:
                if (newStatus == TournamentStatus.PUBLISHED) {
                    changeLegal = true;
                }
                break;
            case PUBLISHED:
                if (newStatus == TournamentStatus.IN_PROGRESS) {
                    changeLegal = true;
                }
                break;
            case IN_PROGRESS:
                if (newStatus == TournamentStatus.FINISHED) {
                    changeLegal = true;
                }
                break;
        }

        if (!changeLegal) 
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tournament status '" + newStatus.toString() + "' is not correct fot next stage for status '" + tournament.getStatus().toString() + "'.");
        }
    
        tournament.setStatus(newStatus);
        return dtoConverter.toTournamentDto(tournament); 
    }

    public void addParticipant(Long tournamentId, UUID userId, String username) 
    {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament not found"));

        if (tournament.getStatus() != TournamentStatus.PENDING && tournament.getStatus() != TournamentStatus.PUBLISHED) 
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot join a tournament that is in progress or finished.");
        }

        if (tournament.getParticipantsIds().contains(userId)) 
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User has already joined this tournament.");
        }

        tournament.getParticipantsIds().add(userId);
        tournament.getParticipantsUsernames().add(username);
        
        tournamentRepository.save(tournament);
    }

    public List<PlayerDto> getParticipants(Long tournamentId) 
    {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament not found"));
    
        if (tournament.getParticipantsUsernames() == null) return new ArrayList<>();
    
        List<String> usernames = tournament.getParticipantsUsernames();
        List<UUID> ids = tournament.getParticipantsIds();
    
        List<PlayerDto> participants = new ArrayList<>();
        for (int i = 0; i < usernames.size(); i++) 
        {
            participants.add(new PlayerDto(ids.get(i), usernames.get(i)));
        }
        
        return participants;
    }

    // Metody Pomocnicze 
    
    private void initializeScoreboard(Tournament tournament) 
    {
        if (tournament.getParticipantsIds() == null) return;

        Map<String, UUID> idsByUsername = new HashMap<>();
        List<String> usernames = new ArrayList<>(tournament.getParticipantsUsernames());
        List<UUID> ids = new ArrayList<>(tournament.getParticipantsIds());
        for (int i = 0; i < usernames.size(); i++) 
        {
            idsByUsername.put(usernames.get(i), ids.get(i));
        }

        for (String username : tournament.getParticipantsUsernames()) 
        {
            Scoreboard entry = Scoreboard.builder()
                .userKeycloakId(idsByUsername.get(username))
                .username(username)
                .points(0f)
                .achievements(new HashMap<>())
                .build();
            tournament.addScoreboardEntry(entry); 
        }
    }
}

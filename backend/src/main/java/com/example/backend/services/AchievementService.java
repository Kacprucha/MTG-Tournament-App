package com.example.backend.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.AchievementDto;
import com.example.backend.entities.Achievement;
import com.example.backend.entities.Tournament;
import com.example.backend.mapping.AchievementMapper;
import com.example.backend.repository.AchievementRepository;
import com.example.backend.repository.TournamentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AchievementService 
{
    private final AchievementRepository achievementRepository;
    private final AchievementMapper achievementMapper;
    
    private final TournamentRepository tournamentRepository;

    public AchievementDto createAchievement(AchievementDto createDto) 
    {
        Long tournamentId = createDto.getTournamentId();

        if (tournamentId == null) 
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tournament ID is required.");
        }

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament not found."));

        Achievement newAchievement = new Achievement();
        newAchievement.setName(createDto.getName());
        newAchievement.setPrice(createDto.getPrice());
        
        tournament.addAchievement(newAchievement); 
        
        Achievement savedAchievement = achievementRepository.save(newAchievement);

        return achievementMapper.toDto(savedAchievement);
    }

    public AchievementDto getAchievementById(Long id) 
    {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Achievement not found."));
        
        return achievementMapper.toDto(achievement);
    }

    public List<AchievementDto> findAchievementsByTournament(Long tournamentId) 
    {
        if (!tournamentRepository.existsById(tournamentId)) 
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament not found.");
        }

        return achievementRepository.findByTournamentId(tournamentId).stream()
                .map(achievementMapper::toDto)
                .collect(Collectors.toList());
    }

    public AchievementDto assignWinner(Long achievementId, UUID winnerId) 
    {
        Achievement achievement = achievementRepository.findById(achievementId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Achievement not found."));
    
        Tournament tournament = achievement.getTournament();
    
        int winnerIndex = tournament.getParticipantsIds().indexOf(winnerId);

        if (winnerIndex == -1) 
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Winner is not a participant of this tournament.");
        }
    
        String winnerUsername = tournament.getParticipantsUsernames().get(winnerIndex);
    
        achievement.setWinnerKeycloakId(winnerId);
        achievement.setWinnerUsername(winnerUsername);
    
        Achievement updatedAchievement = achievementRepository.save(achievement);
        return achievementMapper.toDto(updatedAchievement);
    }

    public void deleteAchievement(Long id) 
    {
        if (!achievementRepository.existsById(id)) 
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Achievement not found.");
        }

        achievementRepository.deleteById(id);
    }
}

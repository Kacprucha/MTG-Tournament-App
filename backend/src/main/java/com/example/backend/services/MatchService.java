package com.example.backend.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.MatchDto;
import com.example.backend.entities.Achievement;
import com.example.backend.entities.Match;
import com.example.backend.entities.MatchAchievement;
import com.example.backend.entities.MatchStatus;
import com.example.backend.entities.Tournament;
import com.example.backend.mapping.MatchMapper;
import com.example.backend.repository.AchievementRepository;
import com.example.backend.repository.MatchRepository;
import com.example.backend.repository.TournamentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchService 
{
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;

    private final AchievementRepository achievementRepository; 
    private final TournamentRepository tournamentRepository;
    private final ScoreboardService scoreboardService;

    public MatchDto createMatch(MatchDto createDto) 
    {
        Tournament tournament = tournamentRepository.findById(createDto.getTournamentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament not found."));

        Match match = new Match();
        match.setTournament(tournament);
        match.setRound(createDto.getRound());
        match.setTableNumber(createDto.getTableNumber());
        match.setBestOf(createDto.getBestOf());
        match.setStatus(MatchStatus.PENDING); // Zawsze zaczynamy jako pending

        // Ustawianie uczestników
        match.setParticipantIds(createDto.getParticipantIds());
        match.setParticipantUsernames(createDto.getParticipantUsernames());

        Match savedMatch = matchRepository.save(match);

        return matchMapper.toDto(savedMatch);
    }
    
    public MatchDto getMatchById(Long id) 
    {
        return matchRepository.findById(id)
                .map(matchMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not found."));
    }

    public List<MatchDto> findMatchesByTournament(Long tournamentId) 
    {
        return matchRepository.findByTournamentId(tournamentId).stream()
                .map(matchMapper::toDto)
                .collect(Collectors.toList());
    }

    public MatchDto updateMatchResults(Long id, MatchDto updateDto) 
    {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not found."));

        match.setGameWinners(updateDto.getGameWinners());
        match.setStatus(updateDto.getStatus());
        
        determineMatchWinner(match);

        updateAchievementsFromDto(match, updateDto.getAchievements());
        
        Match updatedMatch = matchRepository.save(match);

        if (updatedMatch.getStatus() == MatchStatus.COMPLETED) 
        {
            scoreboardService.updateScoreboardFromMatch(updatedMatch);
        }
        
        return matchMapper.toDto(updatedMatch);
    }
    
    // Metody Pomocnicze
    
    private void determineMatchWinner(Match match) 
    {
        if (match.getGameWinners() == null || match.getGameWinners().isEmpty()) 
        {
            match.setWinnerId(null);
            match.setWinnerUsername(null);
            return;
        }

        // Zliczamy zwycięstwa
        Map<String, Long> winsCount = match.getGameWinners().stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        
        // Znajdujemy zwycięzcę (ten, który wygrał `(bestOf / 2) + 1` gier)
        int winsNeeded = (match.getBestOf() / 2) + 1;
        
        Optional<String> winnerUsernameOpt = winsCount.entrySet().stream()
                .filter(entry -> entry.getValue() >= winsNeeded)
                .map(Map.Entry::getKey)
                .findFirst();

        if (winnerUsernameOpt.isPresent()) 
        {
            String winnerUsername = winnerUsernameOpt.get();
            int winnerIndex = match.getParticipantUsernames().indexOf(winnerUsername);

            if (winnerIndex != -1) 
            {
                match.setWinnerUsername(winnerUsername);
                match.setWinnerId(match.getParticipantIds().get(winnerIndex));
            }
        } else 
        {
            match.setWinnerId(null);
            match.setWinnerUsername(null);
        }
    }
    
    private void updateAchievementsFromDto(Match match, Map<String, Map<Long, Integer>> achievementsDto) 
    {
        if (achievementsDto == null) return;
        
        match.getAchievements().clear();
        
        List<Long> achievementIds = achievementsDto.values().stream()
                .flatMap(innerMap -> innerMap.keySet().stream())
                .distinct() 
                .collect(Collectors.toList());

        Map<Long, Achievement> achievementsById = achievementRepository.findAllById(achievementIds).stream()
                .collect(Collectors.toMap(Achievement::getId, Function.identity()));

        // Iterujemy po zagnieżdżonej mapie z DTO
        achievementsDto.forEach((participantIdStr, values) -> 
        {
            UUID participantId = UUID.fromString(participantIdStr);
            values.forEach((achievementId, value) -> 
            {
                Achievement achievement = achievementsById.get(achievementId);
 
                if (achievement != null) 
                {
                    MatchAchievement matchAchievement = MatchAchievement.builder()
                        .participantId(participantId)
                        .achievement(achievement)
                        .value(value)
                        .build();

                    match.addMatchAchievement(matchAchievement);
                }
            });
        });
    }
}

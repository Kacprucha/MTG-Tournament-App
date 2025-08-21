package com.example.backend.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.backend.entities.Achievement;
import com.example.backend.entities.Match;
import com.example.backend.entities.MatchAchievement;
import com.example.backend.entities.Scoreboard;
import com.example.backend.entities.Tournament;
import com.example.backend.repository.ScoreboardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScoreboardService 
{
    private final ScoreboardRepository scoreboardRepository;

    private static final int POINTS_FOR_WIN = 1;

    public void initializeScoreboardForTournament(Tournament tournament) 
    {
        for (int i = 0; i < tournament.getParticipantsIds().size(); i++) 
        {
            UUID participantId = tournament.getParticipantsIds().get(i);
            String participantUsername = tournament.getParticipantsUsernames().get(i);

            Scoreboard entry = Scoreboard.builder()
                    .tournament(tournament)
                    .userKeycloakId(participantId)
                    .username(participantUsername)
                    .points(0)
                    .achievements(new HashMap<>())
                    .build();
            
            scoreboardRepository.save(entry);
        }
    }

    public void updateScoreboardFromMatch(Match match) 
    {
        Tournament tournament = match.getTournament();
        
        // Aktualizuj punkty
        if (match.getWinnerId() != null) 
        {
            updatePointsForPlayer(tournament.getId(), match.getWinnerId(), POINTS_FOR_WIN); 
        }
        
        // Aktualizuj osiągnięcia
        updateAchievementsFromMatch(match);
    }


    // Metody pomocnicze
    private void updatePointsForPlayer(Long tournamentId, UUID playerId, int pointsToAdd) 
    {
        scoreboardRepository.findByTournamentIdAndUserKeycloakId(tournamentId, playerId)
            .ifPresent(entry -> {
                entry.setPoints(entry.getPoints() + pointsToAdd);
            });
    }

    private void updateAchievementsFromMatch(Match match) 
    {
        Map<UUID, List<MatchAchievement>> achievementsByParticipant = match.getAchievements().stream()
            .collect(Collectors.groupingBy(MatchAchievement::getParticipantId));
            
        achievementsByParticipant.forEach((participantId, playerMatchAchievements) -> 
        {
            scoreboardRepository.findByTournamentIdAndUserKeycloakId(match.getTournament().getId(), participantId)
                .ifPresent(entry -> 
                {
                    Map<Long, Integer> currentScoreboardAchievements = entry.getAchievements();
                    
                    for (MatchAchievement ma : playerMatchAchievements) 
                    {
                        Achievement definition = ma.getAchievement();
                        Long achievementId = definition.getId();
                        Integer matchValue = ma.getValue();

                        Integer currentValue = currentScoreboardAchievements.getOrDefault(achievementId, 0);
                        
                        int newValue = switch (definition.getAggregationType()) 
                        {
                            case SUM -> currentValue + matchValue;
                            case MAX -> Math.max(currentValue, matchValue);
                            case MIN -> (currentValue == 0 && matchValue != 0) ? matchValue : Math.min(currentValue, matchValue);
                        };

                        currentScoreboardAchievements.put(achievementId, newValue);
                    }
                });
        });
    }
}

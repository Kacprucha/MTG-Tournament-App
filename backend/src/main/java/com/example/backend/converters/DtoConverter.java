package com.example.backend.converters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.backend.dto.AchievementDto;
import com.example.backend.dto.MatchDto;
import com.example.backend.dto.ScoreboardDto;
import com.example.backend.dto.TournamentDto;
import com.example.backend.entities.Achievement;
import com.example.backend.entities.Match;
import com.example.backend.entities.MatchAchievement;
import com.example.backend.entities.Scoreboard;
import com.example.backend.entities.Tournament;

@Component
public class DtoConverter 
{
    public TournamentDto toTournamentDto(Tournament tournament) 
    {
        if (tournament == null) return null;

        // --- Konwersja list zagnieżdżonych ---
        List<ScoreboardDto> scoreboardDtos = (tournament.getScoreboard() != null)
            ? tournament.getScoreboard().stream().map(this::toScoreboardDto).collect(Collectors.toList())
            : new ArrayList<>();
            
        List<MatchDto> matchDtos = (tournament.getMatches() != null)
            ? tournament.getMatches().stream().map(this::toMatchDto).collect(Collectors.toList())
            : new ArrayList<>();
            
        List<AchievementDto> achievementDtos = (tournament.getAchievements() != null)
            ? tournament.getAchievements().stream().map(this::toAchievementDto).collect(Collectors.toList())
            : new ArrayList<>();

        return TournamentDto.builder()
                .id(tournament.getId())
                .status(tournament.getStatus())
                .name(tournament.getName())
                .type(tournament.getType())
                .addon(tournament.getAddon())
                .date(tournament.getDate())
                .participantIds(tournament.getParticipantsIds())
                .participantUsernames(tournament.getParticipantsUsernames())
                .scoreboard(scoreboardDtos)
                .matches(matchDtos)
                .achievements(achievementDtos) 
                .build();
    }

    public ScoreboardDto toScoreboardDto(Scoreboard scoreboard) 
    {
        if (scoreboard == null) return null;
        
        Map<String, Integer> achievements = (scoreboard.getAchievements() != null)
            ? scoreboard.getAchievements().entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().toString(), Map.Entry::getValue))
            : new HashMap<>();

        return ScoreboardDto.builder()
                .id(scoreboard.getId())
                .userKeycloakId(scoreboard.getUserKeycloakId())
                .username(scoreboard.getUsername())
                .points(scoreboard.getPoints())
                .achievements(scoreboard.getAchievements())
                .build();
    }
    
    public MatchDto toMatchDto(Match match) 
    {
        if (match == null) {
        return null;
    }

    Long tournamentId = (match.getTournament() != null) ? match.getTournament().getId() : null;
    String tournamentName = (match.getTournament() != null) ? match.getTournament().getName() : null;
    
    Map<String, Map<Long, Integer>> achievements = new HashMap<>();
    if (match.getAchievements() != null) {
        // Ta logika działa idealnie zarówno dla List, jak i Set
        achievements = match.getAchievements().stream()
            .collect(Collectors.groupingBy(
                ach -> ach.getParticipantId().toString(),
                Collectors.toMap(
                    ach -> ach.getAchievement().getId(),
                    MatchAchievement::getValue
                )
            ));
    }

    return MatchDto.builder()
            .id(match.getId())
            .tournamentId(tournamentId)
            .tournamentName(tournamentName)
            .status(match.getStatus())
            .type(match.getType())
            .round(match.getRound())
            .tableNumber(match.getTableNumber())
            .bestOf(match.getBestOf())
            .participantIds(match.getParticipantIds())
            .participantUsernames(match.getParticipantUsernames())
            .winnerId(match.getWinnerId())
            .winnerUsername(match.getWinnerUsername())
            .gameWinners(match.getGameWinners())
            .achievements(achievements)
            .build();
    }

    public AchievementDto toAchievementDto(Achievement achievement) 
    {
        if (achievement == null) {
            return null;
        }
        
        Long tournamentId = (achievement.getTournament() != null) ? achievement.getTournament().getId() : null;

        return AchievementDto.builder()
                .id(achievement.getId())
                .tournamentId(tournamentId)
                .name(achievement.getName())
                .price(achievement.getPrice())
                .aggregationType(achievement.getAggregationType())
                .winnerId(achievement.getWinnerKeycloakId()) 
                .winnerUsername(achievement.getWinnerUsername())
                .build();
    }
}

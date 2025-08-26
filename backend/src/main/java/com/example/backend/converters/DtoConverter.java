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
                .participantIds(new ArrayList<>(tournament.getParticipantsIds())) 
                .participantUsernames(new ArrayList<>(tournament.getParticipantsUsernames()))
                .scoreboard(scoreboardDtos)
                .matches(matchDtos)
                .achievements(achievementDtos) 
                .build();
    }

    public ScoreboardDto toScoreboardDto(Scoreboard scoreboard) {
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
                .achievements(achievements)
                .build();
    }
    
    public MatchDto toMatchDto(Match match) { return MatchDto.builder().id(match.getId()).build(); }
    public AchievementDto toAchievementDto(Achievement achievement) {
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

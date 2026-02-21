package com.example.backend.services;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.configuration.GlobalPrefs;
import com.example.backend.dto.FinishConfirmMessage;
import com.example.backend.dto.GameStateMessage;
import com.example.backend.dto.MatchDto;
import com.example.backend.dto.WinnerSelectedMessage;
import com.example.backend.entities.AchivementInScoreboard;
import com.example.backend.entities.Match;
import com.example.backend.entities.MatchParticipant;
import com.example.backend.entities.MatchStatus;
import com.example.backend.entities.Player;
import com.example.backend.entities.Scoreboard;
import com.example.backend.mapping.MatchMapper;
import com.example.backend.repository.AchivementInScoreboardRepository;
import com.example.backend.repository.MatchParticipantRepository;
import com.example.backend.repository.MatchRepository;
import com.example.backend.repository.PlayerRepository;
import com.example.backend.repository.ScoreboardRepository;
import com.example.backend.repository.TournamentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService 
{
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;

    private final TournamentRepository tournamentRepository;
    private final MatchParticipantRepository matchParticipantRepository;
    private final PlayerRepository playerRepository;
    private final ScoreboardRepository scoreboardRepository;
    private final AchivementInScoreboardRepository achivementInScoreboardRepository;

    private final SimpMessagingTemplate messagingTemplate; 
    private final NotificationService notificationService;

    public MatchDto createMatch(MatchDto createDto, List<Long> participantIds) 
    {
        if (matchRepository.existsById(createDto.getId())) 
        {
            throw new IllegalArgumentException("Match with ID " + createDto.getId() + " already exists.");
        }

        if (!tournamentRepository.existsById(createDto.getTournamentId())) 
        {
            throw new IllegalArgumentException("Tournament with ID " + createDto.getTournamentId() + " does not exist.");
        }

        if (createDto.getStatus() != MatchStatus.PENDING) 
        {
            throw new IllegalArgumentException("New matches must have status PENDING.");
        }

        Player participant1 = playerRepository.findById(participantIds.get(0))
                .orElseThrow(() -> new IllegalArgumentException("Player with ID " + participantIds.get(0) + " does not exist."));
        Player participant2 = playerRepository.findById(participantIds.get(1))
                .orElseThrow(() -> new IllegalArgumentException("Player with ID " + participantIds.get(1) + " does not exist."));

        Match match = matchMapper.toEntity(createDto);
        
        MatchParticipant mp1 = new MatchParticipant();
        mp1.setMatch(match);
        mp1.setPlayer(participant1);
        mp1.setWinner(false);

        MatchParticipant mp2 = new MatchParticipant();
        mp2.setMatch(match);
        mp2.setPlayer(participant2);
        mp2.setWinner(false);

        Match savedMatch = matchRepository.save(match);
        matchParticipantRepository.save(mp1);
        matchParticipantRepository.save(mp2);

        return matchMapper.toDto(savedMatch);
    }
    
    public MatchDto getMatchById(Long id) 
    {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not found with ID: " + id));

        return matchMapper.toDto(match);
    }

    public List<MatchDto> findMatchesByTournament(Long tournamentId) 
    {
        List<Match> matches = matchRepository.findByTournamentId(tournamentId);

        return matches.stream()
                .map(matchMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<MatchDto> findMatchesByTournamentAndParticipant(Long tournamentId, Long playerId) 
    {
        List<MatchParticipant> matches = matchRepository.findByPlayerIdAndMatchTournamentId(playerId, tournamentId);
        
        return matches.stream()
                .map(MatchParticipant::getMatch)
                .map(matchMapper::toDto)
                .collect(Collectors.toList());
    }

    public MatchDto updateMatchResults(Long id, MatchDto updateDto) 
    {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not found with ID: " + id));

        if (updateDto.getStatus() == MatchStatus.COMPLETED) 
        {
            throw new IllegalArgumentException("Cannot set match status to COMPLETED through this endpoint.");
        }

        matchMapper.updateEntity(updateDto, match);
        Match updatedMatch = matchRepository.save(match);

        return matchMapper.toDto(updatedMatch);
    }

    public Optional<MatchDto> findNextMatchForUser(Long tournamentId, Long playerId) 
    {
        List<MatchParticipant> matches = matchRepository.findByPlayerIdAndMatchTournamentId(playerId, tournamentId);
        
        return matches.stream()
                .map(MatchParticipant::getMatch)
                .filter(m -> m.getMatchStatus() == MatchStatus.PENDING || m.getMatchStatus() == MatchStatus.IN_PROGRESS)
                .min (Comparator.comparing(Match::getRound))
                .map(matchMapper::toDto);   
    }

    @Transactional
    public void startMatch(Long matchId, Long userId) 
    {
        log.info("--- SERVICE: Entering startMatch for matchId: {} and userId: {} ---", matchId, userId);
        Match match = matchRepository.findById(matchId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not found"));
        
        log.info("Found match with status: {}", match.getBestOf());
        if (matchRepository.findByMatchIdAndPlayerId(matchId, userId) == null) 
        {
            List<Long> participantIds = matchRepository.findByMatchId(matchId).stream()
                                        .map(mp -> mp.getPlayer().getId())
                                        .collect(Collectors.toList());

            log.warn("User {} is not a participant in this match. Participants: {}", userId, participantIds);
            messagingTemplate.convertAndSendToUser(userId.toString(), "/queue/errors", "Not a participant");
            return;
        }

        if (match.getMatchStatus() != MatchStatus.PENDING) 
        {
            log.warn("Match is not in PENDING state. Current status: {}", match.getMatchStatus().toString());
            return; 
        }

        log.info("Updating match status to IN_PROGRESS...");
        match.setMatchStatus(MatchStatus.IN_PROGRESS);

        matchRepository.saveAndFlush(match);

        String destination = "/topic/match/" + matchId;
        GameStateMessage payload = new GameStateMessage("GAME_STARTED");

        log.info("Sending WebSocket message to destination: {} with payload: {}", destination, payload);
        messagingTemplate.convertAndSend(destination, payload);
        log.info("WebSocket message sent.");
    }

    @Transactional
    public void finishMatch(Long matchId, Long reportingUserId, WinnerSelectedMessage payload) 
    {
        Match match = matchRepository.findById(matchId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not found"));
        
        if (matchRepository.findByMatchIdAndPlayerId(matchId, reportingUserId) == null) 
        {
            messagingTemplate.convertAndSendToUser(reportingUserId.toString(), "/queue/errors", "You are not a participant of this match.");
            return; 
        }

        if (match.getMatchStatus() != MatchStatus.IN_PROGRESS) 
        {
            messagingTemplate.convertAndSendToUser(reportingUserId.toString(), "/queue/errors", "Match is not in progress.");
            return;
        }

        String chosenWinnerUsername = payload.getChosenWinner();
        long chosenWinnerId = playerRepository.findByUsername(chosenWinnerUsername)
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chosen winner username not found"))
                                        .getId();

        if (matchRepository.findByMatchIdAndPlayerId(matchId, chosenWinnerId) == null) 
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chosen winner is not a participant of this match.");
        }

        match.setMatchStatus(MatchStatus.WATING_FOR_CONFIRMATION);
        matchRepository.saveAndFlush(match);

        Optional<MatchParticipant> winnerParticipant = matchRepository.findByMatchIdAndPlayerId(matchId, chosenWinnerId);
        winnerParticipant.ifPresent(mp -> {
            mp.setWinner(true);
            matchParticipantRepository.save(mp);
        });

        Optional<Scoreboard> winnerScoreboardEntryOpt = scoreboardRepository.findByTournamentIdAndPlayerId(match.getTournament().getId(), chosenWinnerId);
        winnerScoreboardEntryOpt.ifPresent(s -> {
            s.setPoints(s.getPoints() + GlobalPrefs.POINTS_FOR_WIN);
            scoreboardRepository.save(s);
        });

        String destination = "/topic/match/" + matchId;
        
        messagingTemplate.convertAndSend(destination, payload);
        
        notificationService.sendDelayedNotification(
            destination, 
            new GameStateMessage("GAME_FINISHED_REDIRECT"),
            3000 // 3 s
        );
    }

    @Transactional
    public void finishMatchWithBothStats(Long matchId, FinishConfirmMessage confirmPayload) 
    {
        Match match = matchRepository.findById(matchId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not found"));

        if (match.getMatchStatus() != MatchStatus.WATING_FOR_CONFIRMATION) 
        {
            messagingTemplate.convertAndSendToUser(confirmPayload.getConfirmingPlayerUsername(), "/queue/errors", "Match is not waiting for confirmation.");
            return;
        }

        Player playerA = playerRepository.findById(Long.valueOf(confirmPayload.getOriginalReportingPlayerId()))
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player A username not found"));
        Player playerB = playerRepository.findById(Long.valueOf(confirmPayload.getConfirmingPlayerId()))
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player B username not found"));

        confirmPayload.getOriginalReportedStats().forEach((achievementId, value) -> {
            Optional<AchivementInScoreboard> achivementsA = achivementInScoreboardRepository.findByScoreBoardPlayerIdAndAchivementId(playerA.getId(), achievementId);
            achivementsA.ifPresent(a -> {
                a.setPoints(a.getPoints() + value);
                achivementInScoreboardRepository.save(a);
            });
        });

        confirmPayload.getConfirmingPlayerStats().forEach((achievementId, value) -> {
            Optional<AchivementInScoreboard> achivementsB = achivementInScoreboardRepository.findByScoreBoardPlayerIdAndAchivementId(playerB.getId(), achievementId);
            achivementsB.ifPresent(a -> {
                a.setPoints(a.getPoints() + value);
                achivementInScoreboardRepository.save(a);
            });
        });

        match.setMatchStatus(MatchStatus.COMPLETED);
        matchRepository.save(match);

        String destination = "/topic/match/" + matchId;
        messagingTemplate.convertAndSend(destination, new GameStateMessage("GAME_FINISHED_REDIRECT"));
    }
}

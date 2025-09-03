package com.example.backend.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.converters.DtoConverter;
import com.example.backend.dto.FinishConfirmMessage;
import com.example.backend.dto.GameStateMessage;
import com.example.backend.dto.MatchDto;
import com.example.backend.dto.WinnerSelectedMessage;
import com.example.backend.entities.Achievement;
import com.example.backend.entities.Match;
import com.example.backend.entities.MatchAchievement;
import com.example.backend.entities.MatchStatus;
import com.example.backend.entities.Scoreboard;
import com.example.backend.entities.Tournament;
import com.example.backend.entities.TournamentStatus;
import com.example.backend.repository.AchievementRepository;
import com.example.backend.repository.MatchRepository;
import com.example.backend.repository.TournamentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService 
{
    private final MatchRepository matchRepository;
    //private final MatchMapper matchMapper;

    private final AchievementRepository achievementRepository; 
    private final TournamentRepository tournamentRepository;
    private final ScoreboardService scoreboardService;

    private final DtoConverter dtoConverter;

    private final SimpMessagingTemplate messagingTemplate; 
    private final NotificationService notificationService;

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

        return dtoConverter.toMatchDto(savedMatch);
    }
    
    public MatchDto getMatchById(Long id) 
    {
        return matchRepository.findById(id)
                .map(dtoConverter::toMatchDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not found."));
    }

    public List<MatchDto> findMatchesByTournament(Long tournamentId) 
    {
        return matchRepository.findByTournamentId(tournamentId).stream()
                .map(dtoConverter::toMatchDto)
                .collect(Collectors.toList());
    }

    public List<MatchDto> findMatchesByTournamentAndParticipant(Long tournamentId, String participantUsername) 
    {
        return matchRepository.findByTournamentIdAndParticipantUsernamesContaining(tournamentId, participantUsername).stream()
                .map(dtoConverter::toMatchDto)
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
        
        return dtoConverter.toMatchDto(updatedMatch);
    }

    public void startTournamentAndGenerateMatches(Long tournamentId) 
    {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament not found"));

        if (tournament.getStatus() == TournamentStatus.FINISHED || tournament.getStatus() == TournamentStatus.CANCELLED) 
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot generate pairings for a finished or cancelled tournament.");
        }

        if (!tournament.getMatches().isEmpty()) 
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Matches have already been generated for this tournament.");
        }
        
        List<UUID> participantIds = tournament.getParticipantsIds();
        List<String> participantUsernames = tournament.getParticipantsUsernames();

        if (participantIds.size() < 2) 
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot generate pairings for less than 2 participants.");
        }

        initializeScoreboardForTournament(tournament);
        
        record Participant(UUID id, String username) {}
        List<Participant> participants = new ArrayList<>();
        for (int i = 0; i < participantIds.size(); i++) 
        {
            participants.add(new Participant(participantIds.get(i), participantUsernames.get(i)));
        }
        
        Collections.shuffle(participants);

        // Jeśli mamy nieparzystą liczbę graczy, dodajemy "wirtualnego" gracza 
        if (participants.size() % 2 != 0) 
        {
            participants.add(new Participant(null, "BYE"));
        }

        int numPlayers = participants.size();
        int numRounds = numPlayers - 1;
        
        List<Match> newMatches = new ArrayList<>();
        
        for (int round = 0; round < numRounds; round++) 
        {
            int currentRoundNumber = round + 1;
            
            for (int i = 0; i < numPlayers / 2; i++) 
            {
                Participant player1 = participants.get(i);
                Participant player2 = participants.get(numPlayers - 1 - i);

                // Jeśli któryś z graczy to "BYE", tworzymy mecz z wolnym losem
                if ("BYE".equals(player1.username())) 
                {
                    createByeMatch(tournament, currentRoundNumber, player2.id(), player2.username());
                    continue;
                }

                if ("BYE".equals(player2.username())) 
                {
                    createByeMatch(tournament, currentRoundNumber, player1.id(), player1.username());
                    continue;
                }

                // Tworzymy normalny mecz
                Match match = Match.builder()
                        .status(MatchStatus.PENDING)
                        .type("runa turniejowa")
                        .bestOf(1)
                        .round(currentRoundNumber)
                        .participantIds(new ArrayList<>(List.of(player1.id(), player2.id())))
                        .participantUsernames(new ArrayList<>(List.of(player1.username(), player2.username())))
                        .build();
                
                tournament.addMatch(match);
                newMatches.add(match);
            }
            
            // "Obracamy karuzelę": ostatni gracz zostaje na miejscu, reszta przesuwa się w lewo
            Participant last = participants.remove(participants.size() - 1);
            participants.add(1, last);
        }

        tournament.setStatus(TournamentStatus.IN_PROGRESS);

        matchRepository.saveAll(newMatches);
        tournamentRepository.save(tournament);
    }

    public Optional<MatchDto> findNextMatchForUser(Long tournamentId, UUID userId) 
    {
        List<Match> userMatches = matchRepository.findByTournamentIdAndParticipantIdsContaining(tournamentId, userId);
    
        return userMatches.stream()
            .filter(m -> m.getStatus() == MatchStatus.PENDING || m.getStatus() == MatchStatus.IN_PROGRESS)
            .min(Comparator.comparing(Match::getRound)) 
            .map(dtoConverter::toMatchDto);
    }

    @Transactional
    public void startMatch(Long matchId, UUID userId) 
    {
        log.info("--- SERVICE: Entering startMatch for matchId: {} and userId: {} ---", matchId, userId);
        Match match = matchRepository.findById(matchId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not found"));
        
        log.info("Found match with status: {}", match.getStatus());
        if (!match.getParticipantIds().contains(userId)) 
        {
            log.warn("User {} is not a participant in this match. Participants: {}", userId, match.getParticipantIds());
            messagingTemplate.convertAndSendToUser(userId.toString(), "/queue/errors", "Not a participant");
            return;
        }

        if (match.getStatus() != MatchStatus.PENDING) 
        {
            log.warn("Match is not in PENDING state. Current status: {}", match.getStatus());
            return; 
        }

        log.info("Updating match status to IN_PROGRESS...");
        match.setStatus(MatchStatus.IN_PROGRESS);

        matchRepository.saveAndFlush(match);
        
        String destination = "/topic/match/" + matchId;
        GameStateMessage payload = new GameStateMessage("GAME_STARTED");

        log.info("Sending WebSocket message to destination: {} with payload: {}", destination, payload);
        messagingTemplate.convertAndSend(destination, payload);
        log.info("WebSocket message sent.");
    }

    @Transactional
    public void finishMatch(Long matchId, UUID reportingUserId, WinnerSelectedMessage payload) 
    {
        Match match = matchRepository.findById(matchId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not found"));
        
        if (!match.getParticipantIds().contains(reportingUserId)) 
        {
            messagingTemplate.convertAndSendToUser(reportingUserId.toString(), "/queue/errors", "You are not a participant of this match.");
            return; 
        }

        if (match.getStatus() != MatchStatus.IN_PROGRESS) 
        {
            return;
        }

        String chosenWinnerUsername = payload.getChosenWinner();
        int winnerIndex = match.getParticipantUsernames().indexOf(chosenWinnerUsername);
        if (winnerIndex == -1) 
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chosen winner is not a participant of this match.");
        }
        UUID chosenWinnerId = match.getParticipantIds().get(winnerIndex);

        match.getGameWinners().add(chosenWinnerUsername);
        match.setWinnerId(chosenWinnerId);
        match.setWinnerUsername(chosenWinnerUsername);
        match.setStatus(MatchStatus.COMPLETED);

        scoreboardService.updateScoreboardFromMatch(match);

        String destination = "/topic/match/" + matchId;
        
        messagingTemplate.convertAndSend(destination, payload);
        
        notificationService.sendDelayedNotification(
            destination, 
            new GameStateMessage("GAME_FINISHED_REDIRECT"),
            3000 // 3000 milisekund = 3 sekundy
        );
    }

    @Transactional
    public void finishMatchWithBothStats(Long matchId, FinishConfirmMessage confirmPayload) 
    {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not found"));
        
        if (match.getStatus() != MatchStatus.IN_PROGRESS) 
        {
            return;
        }

        UUID reportingPlayerUUID = UUID.fromString(confirmPayload.getOriginalReportingPlayerId());
        UUID confirmingPlayerUUID = UUID.fromString(confirmPayload.getConfirmingPlayerId());

        if (!match.getParticipantIds().containsAll(List.of(reportingPlayerUUID, confirmingPlayerUUID))) 
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more players are not participants of this match.");
        }
        
        match.getAchievements().clear(); // Wyczyść stare wpisy na wszelki wypadek

        // Dodaj osiągnięcia Gracza A (raportującego)
        confirmPayload.getOriginalReportedStats().forEach((achievementId, value) -> {
            Achievement achievement = achievementRepository.getReferenceById(achievementId); // Pobierz referencję
            MatchAchievement matchAchievement = MatchAchievement.builder()
                                .match(match)
                                .participantId(reportingPlayerUUID)
                                .achievement(achievement)
                                .value(value)
                                .build();
            match.addMatchAchievement(matchAchievement);
        });
        
        // Dodaj osiągnięcia Gracza B (potwierdzającego)
        confirmPayload.getConfirmingPlayerStats().forEach((achievementId, value) -> {
            Achievement achievement = achievementRepository.getReferenceById(achievementId);
            MatchAchievement matchAchievement = MatchAchievement.builder()
                                .match(match)
                                .participantId(reportingPlayerUUID)
                                .achievement(achievement)
                                .value(value)
                                .build();
            match.addMatchAchievement(matchAchievement);
        });

        // Ustaw zwycięzcę i status
        String winnerUsername = confirmPayload.getChosenWinnerUsername();
        int winnerIndex = match.getParticipantUsernames().indexOf(winnerUsername);
        if (winnerIndex != -1) {
            match.setWinnerId(match.getParticipantIds().get(winnerIndex));
            match.setWinnerUsername(winnerUsername);
            // Możesz tu też zaktualizować `gameWinners`
        }
        match.setStatus(MatchStatus.COMPLETED);
        
        scoreboardService.updateScoreboardFromMatch(match);
        
        String destination = "/topic/match/" + matchId;
        messagingTemplate.convertAndSend(destination, new GameStateMessage("GAME_FINISHED_REDIRECT"));
        
        // Nie ma potrzeby `save()`, transakcja zatwierdzi wszystkie zmiany.
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

    private void createByeMatch(Tournament tournament, int roundNumber, UUID playerId, String username) {
        Match byeMatch = Match.builder()
                .status(MatchStatus.COMPLETED)
                .type("BYE")
                .round(roundNumber)
                .bestOf(1)
                .participantIds(List.of(playerId))
                .participantUsernames(new ArrayList<>(List.of(username)))
                .winnerId(playerId) 
                .winnerUsername(username)
                .gameWinners(new ArrayList<>(List.of(username)))
                .build();
        tournament.addMatch(byeMatch);
    }

    private void initializeScoreboardForTournament(Tournament tournament) {
        Map<Long, Integer> initialAchievements = tournament.getAchievements().stream()
                .collect(Collectors.toMap(Achievement::getId, ach -> 0));

        Map<String, UUID> idsByUsername = new HashMap<>();
        List<String> usernames = new ArrayList<>(tournament.getParticipantsUsernames());
        List<UUID> ids = new ArrayList<>(tournament.getParticipantsIds());
        for (int i = 0; i < usernames.size(); i++) {
            idsByUsername.put(usernames.get(i), ids.get(i));
        }

        for (String username : tournament.getParticipantsUsernames()) {
            Scoreboard entry = Scoreboard.builder()
                    .userKeycloakId(idsByUsername.get(username))
                    .username(username)
                    .points(0f)
                    .achievements(new HashMap<>(initialAchievements))
                    .build();
            tournament.addScoreboardEntry(entry);
        }
    }
}

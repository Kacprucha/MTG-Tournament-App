package com.example.backend.controllers;

import java.util.UUID;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import com.example.backend.dto.FinishAttemptMessage;
import com.example.backend.dto.FinishConfirmMessage;
import com.example.backend.dto.WinnerSelectedMessage;
import com.example.backend.services.MatchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketController 
{
    private final MatchService matchService;

    @MessageMapping("/match/{matchId}/start")
    public void startGame(
        @DestinationVariable String matchId,
        SimpMessageHeaderAccessor headerAccessor 
    ) 
    {
        log.info("--- CONTROLLER: Received START message for match {} ---", matchId);
        
        // --- OSTATECZNY DEBUG ---
        java.security.Principal user = headerAccessor.getUser();
        
        if (user == null) {
            log.error("CRITICAL: headerAccessor.getUser() returned NULL. User is not authenticated in WebSocket session.");
            return;
        }

        log.info("headerAccessor.getUser() returned Principal of type: {}", user.getClass().getName());
        log.info("Principal name: {}", user.getName());
        
        // Sprawdźmy, czy to jest obiekt Authentication
        if (!(user instanceof Authentication)) {
            log.error("CRITICAL: Principal is NOT an instance of Authentication.");
            return;
        }
        // ------------------------

        try {
            Authentication authentication = (Authentication) user;
            Object principal = authentication.getPrincipal();

            log.info("Authentication.getPrincipal() returned object of type: {}", principal.getClass().getName());

            if (!(principal instanceof Jwt)) {
                log.error("CRITICAL: Principal object is NOT an instance of Jwt.");
                return;
            }

            Jwt jwt = (Jwt) principal;
            UUID userId = UUID.fromString(jwt.getSubject());
            
            log.info("Successfully extracted userId: {}. Calling service...", userId);
            matchService.startMatch(Long.parseLong(matchId), userId);
            
        } catch (Exception e) {
            log.error("An unexpected error occurred during principal extraction.", e);
        }
    }

    @MessageMapping("/match/{matchId}/finish")
    public void finishMatch(
        @DestinationVariable Long matchId,
        @Payload WinnerSelectedMessage payload, // Przyjmujemy ciało wiadomości
        SimpMessageHeaderAccessor headerAccessor
    ) 
    {
        Authentication authentication = (Authentication) headerAccessor.getUser();
        if (authentication == null) 
        {
            return;
        }

        Jwt principal = (Jwt) authentication.getPrincipal();
        UUID reportingUserId = UUID.fromString(principal.getSubject());

        matchService.finishMatch(matchId, reportingUserId, payload);
    }

    @MessageMapping("/match/{matchId}/finish-attempt")
    @SendTo("/topic/match/{matchId}") // Prześlij dalej do drugiego gracza
    public FinishAttemptMessage finishAttempt(
        @DestinationVariable Long matchId,
        @Payload FinishAttemptMessage payload,
        SimpMessageHeaderAccessor headerAccessor
    ) 
    {
        Jwt jwt = (Jwt) ((Authentication) headerAccessor.getUser()).getPrincipal();
        payload.setReportingPlayerId(jwt.getSubject());
        payload.setReportingPlayerUsername(jwt.getClaimAsString("preferred_username"));
        return payload;
    }

    @MessageMapping("/match/{matchId}/finish-confirm")
    public void finishConfirm(
        @DestinationVariable Long matchId,
        @Payload FinishConfirmMessage payload,
        SimpMessageHeaderAccessor headerAccessor
    ) 
    {
        Jwt jwt = (Jwt) ((Authentication) headerAccessor.getUser()).getPrincipal();
        payload.setConfirmingPlayerId(jwt.getSubject());
        payload.setConfirmingPlayerUsername(jwt.getClaimAsString("preferred_username"));
    
        matchService.finishMatchWithBothStats(matchId, payload);
    }
}

package com.example.backend.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class FinishConfirmMessage 
{
    private String type = "FINISH_CONFIRM";

    // Dane z oryginalnego żądania (od Gracza A)
    private String originalReportingPlayerId;
    private String chosenWinnerUsername;
    private Map<Long, Float> originalReportedStats;
    
    // Dane dodane przez Gracza B
    private String confirmingPlayerId;
    private String confirmingPlayerUsername;
    private Map<Long, Float> confirmingPlayerStats;
}

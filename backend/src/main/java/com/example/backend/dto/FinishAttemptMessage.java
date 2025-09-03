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
public class FinishAttemptMessage 
{
    private String type = "FINISH_ATTEMPT";
    private String reportingPlayerId; 
    private String reportingPlayerUsername;
    private String chosenWinnerUsername;
    private Map<Long, Integer> reportedStats; 
}

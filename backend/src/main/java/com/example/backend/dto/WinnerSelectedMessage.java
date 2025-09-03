package com.example.backend.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@AllArgsConstructor
@NoArgsConstructor
public class WinnerSelectedMessage 
{
    private String type = "WINNER_SELECTED";
    private String selectingPlayer; 
    private String chosenWinner;
    private Map<Long, Integer> reportedStats;
}

package com.example.backend.dto;

import java.time.LocalDate;

public class CreateTournamentRequestDto 
{
    private String name;
    private String type;
    private String addon;
    private LocalDate date;


    public CreateTournamentRequestDto() 
    {

    }

    public String getName() { return name; }
    public String getType() { return type; }
    public String getAddon() { return addon; }
    public LocalDate getDate() { return date; }

    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void setAddon(String addon) { this.addon = addon; }
    public void setDate(LocalDate date) { this.date = date; }
}

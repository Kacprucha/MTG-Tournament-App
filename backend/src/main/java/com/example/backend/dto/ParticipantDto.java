package com.example.backend.dto;

import java.util.UUID;

public class ParticipantDto 
{
    private UUID keycloakId;
    private String username;

    public ParticipantDto() 
    {
    }

    public ParticipantDto(UUID keycloakId, String username) 
    {
        this.keycloakId = keycloakId;
        this.username = username;
    }

    public UUID getKeycloakId() 
    {
        return keycloakId;
    }

    public String getUsername() 
    {
        return username;
    }

    public void setKeycloakId(UUID keycloakId) 
    {
        this.keycloakId = keycloakId;
    }

    public void setUsername(String username) 
    {
        this.username = username;
    }
}

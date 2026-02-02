package com.example.backend.embeddable;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class TournamentParticipantId implements Serializable 
{
    private Long tournament;
    private Long participant;

    public TournamentParticipantId(Long tournament, Long participant) 
    {
        this.tournament = tournament;
        this.participant = participant;
    }

    @Override
    public boolean equals(Object obj) 
    {
        boolean result = true;
        
        if (obj == null || getClass() != obj.getClass())
        {
            result = false;
        }
        else
        {
            TournamentParticipantId that = (TournamentParticipantId) obj;
            
            if (this == obj)
            {
                result = true;
            }
            else if (!this.tournament.equals(that.tournament) || !this.participant.equals(that.participant))
            {
                result = false;
            }
        }

        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tournament, participant);
    }
}

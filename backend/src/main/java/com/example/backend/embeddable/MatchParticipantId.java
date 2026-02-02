package com.example.backend.embeddable;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class MatchParticipantId implements Serializable 
{
    private Long match;
    private Long participant;

    public MatchParticipantId(Long match, Long participant) 
    {
        this.match = match;
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
            MatchParticipantId other = (MatchParticipantId) obj;

            if (this == obj) 
            {
                result = true;
            }
            else if (!Objects.equals(this.match, other.match) || !Objects.equals(this.participant, other.participant)) 
            {
                result = false;
            }
        }

        return result;
    }

    @Override
    public int hashCode() 
    {
        return Objects.hash(match, participant);
    }

    
}

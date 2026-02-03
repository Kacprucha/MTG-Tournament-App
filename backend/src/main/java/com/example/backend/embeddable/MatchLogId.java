package com.example.backend.embeddable;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class MatchLogId implements Serializable
{
    private Long match;
    private Long achievement;

    public MatchLogId(Long march, Long achievement) 
    {
        this.match = march;
        this.achievement = achievement;
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
            MatchLogId other = (MatchLogId) obj;

            if (this == obj) 
            {
                result = true;
            }
            else if (!Objects.equals(this.match, other.match) || !Objects.equals(this.achievement, other.achievement)) 
            {
                result = false;
            }
        }

        return result;
    }

    @Override
    public int hashCode() 
    {
        return Objects.hash(match, achievement);  
    }
}

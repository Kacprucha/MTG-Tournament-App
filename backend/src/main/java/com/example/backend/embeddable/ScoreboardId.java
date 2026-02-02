package com.example.backend.embeddable;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class ScoreboardId implements Serializable 
{
    private Long tournament;
    private Long player;

    public ScoreboardId(Long tournament, Long player) 
    {
        this.tournament = tournament;
        this.player = player;
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
            ScoreboardId that = (ScoreboardId) obj;
            
            if (this == obj)
            {
                result = true;
            }
            else if (!this.tournament.equals(that.tournament) || !this.player.equals(that.player))
            {
                result = false;
            }
        }

        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tournament, player);
    }
}

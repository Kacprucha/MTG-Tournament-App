package com.example.backend.embeddable;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class AchivementInTournamentId implements Serializable
{
    private Long achievement;
    private Long tournament;

    public AchivementInTournamentId(Long achievement, Long tournament) 
    {
        this.achievement = achievement;
        this.tournament = tournament;
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
            AchivementInTournamentId that = (AchivementInTournamentId) obj;
            
            if (this == obj)
            {
                result = true;
            }
            else if (!this.achievement.equals(that.achievement) || !this.tournament.equals(that.tournament))
            {
                result = false;
            }
        }

        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(achievement, tournament);
    }
}

package com.example.backend.embeddable;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class AchivementInScoreboardId implements Serializable
{
    private Long achievement;
    private Long scoreboard;

    public AchivementInScoreboardId(Long achievement, Long scoreboard) 
    {
        this.achievement = achievement;
        this.scoreboard = scoreboard;
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
            AchivementInScoreboardId that = (AchivementInScoreboardId) obj;
            
            if (this == obj)
            {
                result = true;
            }
            else if (!this.achievement.equals(that.achievement) || !this.scoreboard.equals(that.scoreboard))
            {
                result = false;
            }
        }

        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(achievement, scoreboard);
    }
}

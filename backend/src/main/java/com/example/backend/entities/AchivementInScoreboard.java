package com.example.backend.entities;

import com.example.backend.embeddable.AchivementInScoreboardId;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@IdClass(AchivementInScoreboardId.class)
@Table
@Setter
@Getter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AchivementInScoreboard 
{
    @Id
    @ManyToOne
    @JoinColumn(name = "achievement_id", nullable=false)
    private Achievement achievement;

    @Id
    @ManyToOne
    @JoinColumn(name = "scoreboard_id", nullable=false)
    private Scoreboard scoreboard;

    private float points;
}

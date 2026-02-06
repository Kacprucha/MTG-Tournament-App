package com.example.backend.entities;

import com.example.backend.embeddable.ScoreboardId;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@IdClass(ScoreboardId.class)
@Table
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Scoreboard 
{
    @Id
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @Id
    @JoinColumn(name = "player_id")
    private Player player;

    private float points;
}

package com.example.backend.entities;

import com.example.backend.embeddable.ScoreboardId;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Scoreboard 
{
    @EmbeddedId
    private ScoreboardId id;

    @MapsId("tournament")
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @MapsId("player")
    @JoinColumn(name = "player_id")
    private Player player;

    private float points;
}

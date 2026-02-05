package com.example.backend.embeddable;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
@EqualsAndHashCode 
public class ScoreboardId implements Serializable 
{
    @Column(name = "tournament_id")
    private Long tournament;

    @Column(name = "player_id")
    private Long player;
}

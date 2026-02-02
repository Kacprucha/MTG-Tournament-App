package com.example.backend.entities;

import com.example.backend.embeddable.MatchParticipantId;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
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
@Table
@Setter
@Getter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class MatchParticipant 
{
    @EmbeddedId
    private MatchParticipantId id;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable=false)
    private Match match;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable=false)
    private Player player;

    private boolean isWinner;
}

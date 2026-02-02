package com.example.backend.entities;

import com.example.backend.embeddable.AchivementInTournamentId;

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
public class AchivementInTournament 
{
    @EmbeddedId
    private AchivementInTournamentId id;

    @ManyToOne
    @JoinColumn(name = "achievement_id", nullable=false)
    private Achievement achievement;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable=false)
    private Tournament tournament;
}

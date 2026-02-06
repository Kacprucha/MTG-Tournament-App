package com.example.backend.entities;

import java.time.Instant;

import com.example.backend.embeddable.MatchLogId;

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
@IdClass(MatchLogId.class)
@Table
@Setter
@Getter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class MatchLog 
{
    @Id
    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;
    
    @Id
    @ManyToOne
    @JoinColumn(name = "achievement_id", nullable = false)
    private Achievement achievement;
    
    private int count;
    private Instant loggedAt;
    private Instant updatedAt;
}

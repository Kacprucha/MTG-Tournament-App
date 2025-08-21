package com.example.backend.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKey;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "scoreboards")
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Scoreboard 
{
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @Column(nullable = false)
    private UUID userKeycloakId;
    private String username;
    private Integer points;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "scoreboard_achievements", joinColumns = @JoinColumn(name = "scoreboard_id"))
    @MapKeyJoinColumn(name = "achievement_id")
    @Column(name = "value")
    @Builder.Default
    private Map<Long, Integer> achievements = new HashMap<>();
}

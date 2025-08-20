package com.example.backend.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyClass;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "matches")
@Setter
@Getter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Match 
{
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;
    private String type;
    private Integer round;
    private Integer tableNumber;
    private Integer bestOf;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "match_participant_ids", joinColumns = @JoinColumn(name = "match_id"))
    @Column(name = "participant_keycloak_id")
    @OrderColumn
    @Builder.Default
    private List<UUID> participantIds = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "match_participant_usernames", joinColumns = @JoinColumn(name = "match_id"))
    @Column(name = "participant_username")
    @OrderColumn 
    @Builder.Default
    private List<String> participantUsernames = new ArrayList<>();

    @Column(name = "winner_id")
    private UUID winnerId; 
    private String winnerUsername;

    @ElementCollection
    @Builder.Default
    private List<String> gameWinners = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "match_achievements", joinColumns = @JoinColumn(name = "match_id"))
    @MapKeyColumn(name = "participant_keycloak_id")
    @MapKeyClass(UUID.class)
    @Builder.Default
    private Map<UUID, Map<Long, Integer>> achievements = new HashMap<>();
}

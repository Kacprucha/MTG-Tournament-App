package com.example.backend.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
@Table(name = "tiurnaments")
@Setter
@Getter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Tournament
{
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    private String type;
    private String addon;
    private LocalDate date;

    @ElementCollection(fetch= FetchType.LAZY)
    @CollectionTable(name = "tournament_participants", joinColumns = @JoinColumn(name = "tournament_id"))
    @Column(name = "participant_keycloak_id")
    @OrderColumn
    @Builder.Default
    private List<UUID> participantsIds = new ArrayList<>();

    @ElementCollection(fetch= FetchType.LAZY)
    @CollectionTable(name = "tournament_participants_usernames", joinColumns = @JoinColumn(name = "tournament_id"))
    @Column(name = "participant_username")
    @OrderColumn
    @Builder.Default
    private List<String> participantsUsernames = new ArrayList<>();

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Match> matches = new ArrayList<>();
    
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Scoreboard> scoreboard = new ArrayList<>();

    public void addMatch(Match match) {
        matches.add(match);
        match.setTournament(this);
    }

    public void removeMatch(Match match) {
        matches.remove(match);
        match.setTournament(null);
    }

    public void addScoreboardEntry(Scoreboard entry) {
        scoreboard.add(entry);
        entry.setTournament(this);
    }

    public void removeScoreboardEntry(Scoreboard entry) {
        scoreboard.remove(entry);
        entry.setTournament(null);
    }
}

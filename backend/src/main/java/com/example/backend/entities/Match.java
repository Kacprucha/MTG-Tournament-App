package com.example.backend.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
public class Match 
{
    @Id
    @GeneratedValue
    private Long id;

    private String tournamentName;
    private String player1;
    private String player2;
    private Integer tableNumber;
    private String winner;
    private Integer player1Hp;
    private Integer time;
    private List<Integer> player1Achievements;
}

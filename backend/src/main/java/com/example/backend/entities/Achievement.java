package com.example.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Achievement 
{
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String priceDescription;
    private String achievementDescription;

    @ManyToOne
    @JoinColumn(name = "achievement_aggregation_id", nullable = false)
    private AchievementAggregation achievementAggregation;
}

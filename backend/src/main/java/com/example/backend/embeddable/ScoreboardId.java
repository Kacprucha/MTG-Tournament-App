package com.example.backend.embeddable;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
@EqualsAndHashCode 
public class ScoreboardId implements Serializable 
{
    private Long tournament;
    
    private Long player;
}

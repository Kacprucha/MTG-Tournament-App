package com.example.backend.mapping;

import org.mapstruct.Mapper;

import com.example.backend.dto.MatchDto;
import com.example.backend.entities.Match;

@Mapper
public interface MatchMapper 
{
    MatchDto toDto (Match match);

    Match toEntity (MatchDto matchDto);
}
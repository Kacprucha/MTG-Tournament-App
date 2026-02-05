package com.example.backend.mapping;

import org.mapstruct.Mapper;

import com.example.backend.dto.PlayerDto;
import com.example.backend.entities.Player;

@Mapper(componentModel = "spring")
public interface PlayerMapper 
{
    PlayerDto toDto(Player player);

    Player toEntity(PlayerDto playerDto);
}

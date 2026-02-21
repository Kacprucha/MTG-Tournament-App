package com.example.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entities.Player;

public interface PlayerRepository extends JpaRepository<Player, Long>
{
    Optional<Player> findByUsername(String username);
}

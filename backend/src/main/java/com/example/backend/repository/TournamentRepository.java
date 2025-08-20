package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entities.Tournament;

public interface TournamentRepository extends JpaRepository<Tournament, Long>
{

}

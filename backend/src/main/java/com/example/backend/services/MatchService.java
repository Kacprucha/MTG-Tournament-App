package com.example.backend.services;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.MatchDto;
import com.example.backend.entities.Match;
import com.example.backend.entities.Tournament;
import com.example.backend.mapping.MatchMapper;
import com.example.backend.repository.MatchRepository;
import com.example.backend.repository.TournamentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchService 
{
    private final MatchRepository matchRepository;
    private final TournamentRepository tournamentRepository;

    private final MatchMapper matchMapper;

    public MatchDto CreateMatch (MatchDto matchDto) 
    {
        if (matchDto == null || matchDto.getId() != null) 
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide match without id");
        }

        Match entity = matchMapper.toEntity(matchDto);
        Tournament tournament = tournamentRepository.findById(matchDto.getTournamentId()).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament with id " + matchDto.getTournamentId() + " not found.")
        );
        entity.setTournament(tournament);

        return save(matchDto);
    }

    public MatchDto UpdateMatch (Long id, MatchDto matchDto) 
    {
        if (matchDto.getId() == null || !Objects.equals(matchDto.getId(), matchDto.getId())) 
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id and match.id is not equal");
        }

        if (!matchRepository.existsById(id)) 
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Match with id " + id + " not found.");
        }

        return save(matchDto);
    }

    public void DeleteMatch (Long id) 
    {
        if (!matchRepository.existsById(id)) 
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Match with id " + id + " not found");
        }

        matchRepository.deleteById(id);
    }
    
    public Collection<MatchDto> FindMatchesByTournament(String tournamentName) 
    {
        return matchRepository.findByTournamentName(tournamentName).stream()
                .map(matchMapper::toDto)
                .collect(Collectors.toList());
    }

    private MatchDto save (MatchDto matchDto) 
    {
        Match entity = matchMapper.toEntity(matchDto);
        entity = matchRepository.save(entity);
        return matchMapper.toDto(entity);
    }
}

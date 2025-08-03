package com.example.backend.services;

import com.example.backend.dto.MatchDto;
import com.example.backend.entities.Match;
import com.example.backend.mapping.MatchMapper;
import com.example.backend.repository.MatchRepository;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchService 
{
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;

    public MatchDto CrateMatch(MatchDto matchDto) 
    {
        if (matchDto == null || matchDto.getId() != null) 
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide match without id");
        }

        return save(matchDto);
    }

    public MatchDto UpdateMatch (MatchDto matchDto) 
    {
        if (!Objects.equals(matchDto.getId(), matchDto.getId())) 
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id and match.id is not equal");
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
        entity = matchRepository.saveAndFlush(entity);
        return matchMapper.toDto(entity);
    }
}

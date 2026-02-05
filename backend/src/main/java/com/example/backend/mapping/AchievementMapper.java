package com.example.backend.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.backend.dto.AchievementDto;
import com.example.backend.entities.Achievement;
import com.example.backend.entities.AchievementAggregation;

@Mapper(componentModel = "spring")
public interface AchievementMapper 
{
    @Mapping(source = "achievementAggregation.id", target = "achievementAggregationId")
    AchievementDto toDto(Achievement achievement);

    @Mapping(source = "achievementAggregationId", target = "achievementAggregation.id")
    Achievement toEntity(AchievementDto dto);

    @Mapping(source = "achievementAggregationId", target = "achievementAggregation.id")
    void updateEntityFromDto(AchievementDto dto, @MappingTarget Achievement entity);

    default AchievementAggregation mapAchievementAggregation(Long id) 
    {
        if (id == null) return null;

        AchievementAggregation aggregation = new AchievementAggregation();
        aggregation.setId(id);
        
        return aggregation;
    }
}

package com.sporty.jackpot.infrastructure.persistence.mapper;

import com.sporty.jackpot.domain.model.Jackpot;
import com.sporty.jackpot.infrastructure.persistence.entity.JackpotEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JackpotDomainMapper {

    @Mapping(target = "contributionStrategyType", source = "contributionStrategyType")
    @Mapping(target = "rewardStrategyType", source = "rewardStrategyType")
    Jackpot toDomain(JackpotEntity entity);
}

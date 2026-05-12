package com.sporty.jackpot.infrastructure.persistence.mapper;

import com.sporty.jackpot.domain.model.JackpotReward;
import com.sporty.jackpot.infrastructure.persistence.entity.JackpotRewardEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JackpotRewardDomainMapper {
    JackpotRewardEntity toEntity(JackpotReward reward);
}

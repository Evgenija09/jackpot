package com.sporty.jackpot.api.mapper;

import com.sporty.jackpot.api.dto.request.JackpotCreateRequest;
import com.sporty.jackpot.api.dto.response.JackpotResponse;
import com.sporty.jackpot.infrastructure.persistence.entity.JackpotEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JackpotMapper {

    @Mapping(source = "jackpotId", target = "id")
    @Mapping(source = "initialPoolAmount", target = "currentPoolAmount")
    @Mapping(target = "version", constant = "0")
    @Mapping(source = "contributionStrategyType", target = "contributionStrategyType")
    @Mapping(source = "rewardStrategyType", target = "rewardStrategyType")
    JackpotEntity toEntity(JackpotCreateRequest request);

    JackpotResponse toResponse(JackpotEntity entity);
}

package com.sporty.jackpot.infrastructure.persistence.mapper;

import com.sporty.jackpot.domain.model.JackpotContribution;
import com.sporty.jackpot.infrastructure.persistence.entity.JackpotContributionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JackpotContributionDomainMapper {
    JackpotContributionEntity toEntity(JackpotContribution contribution);
}

package com.sporty.jackpot.api.mapper;

import com.sporty.jackpot.api.dto.request.JackpotCreateRequest;
import com.sporty.jackpot.api.dto.response.JackpotResponse;
import com.sporty.jackpot.infrastructure.persistence.entity.JackpotEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-11T21:43:31+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.10 (Microsoft)"
)
@Component
public class JackpotMapperImpl implements JackpotMapper {

    @Override
    public JackpotEntity toEntity(JackpotCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        JackpotEntity jackpotEntity = new JackpotEntity();

        jackpotEntity.setId( request.getJackpotId() );
        jackpotEntity.setCurrentPoolAmount( request.getInitialPoolAmount() );
        if ( request.getContributionStrategyType() != null ) {
            jackpotEntity.setContributionStrategyType( request.getContributionStrategyType().name() );
        }
        if ( request.getRewardStrategyType() != null ) {
            jackpotEntity.setRewardStrategyType( request.getRewardStrategyType().name() );
        }
        jackpotEntity.setInitialPoolAmount( request.getInitialPoolAmount() );
        jackpotEntity.setContributionRate( request.getContributionRate() );
        jackpotEntity.setContributionDecayRate( request.getContributionDecayRate() );
        jackpotEntity.setRewardChance( request.getRewardChance() );
        jackpotEntity.setRewardGrowthRate( request.getRewardGrowthRate() );
        jackpotEntity.setRewardPoolLimit( request.getRewardPoolLimit() );

        jackpotEntity.setVersion( 0 );

        return jackpotEntity;
    }

    @Override
    public JackpotResponse toResponse(JackpotEntity entity) {
        if ( entity == null ) {
            return null;
        }

        JackpotResponse jackpotResponse = new JackpotResponse();

        return jackpotResponse;
    }
}

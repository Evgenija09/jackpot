package com.sporty.jackpot.infrastructure.persistence.mapper;

import com.sporty.jackpot.domain.model.Jackpot;
import com.sporty.jackpot.domain.strategy.contribution.ContributionStrategyType;
import com.sporty.jackpot.domain.strategy.reward.RewardStrategyType;
import com.sporty.jackpot.infrastructure.persistence.entity.JackpotEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-12T09:17:53+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.10 (Microsoft)"
)
@Component
public class JackpotDomainMapperImpl implements JackpotDomainMapper {

    @Override
    public Jackpot toDomain(JackpotEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Jackpot jackpot = new Jackpot();

        if ( entity.getContributionStrategyType() != null ) {
            jackpot.setContributionStrategyType( Enum.valueOf( ContributionStrategyType.class, entity.getContributionStrategyType() ) );
        }
        if ( entity.getRewardStrategyType() != null ) {
            jackpot.setRewardStrategyType( Enum.valueOf( RewardStrategyType.class, entity.getRewardStrategyType() ) );
        }
        jackpot.setId( entity.getId() );
        jackpot.setInitialPoolAmount( entity.getInitialPoolAmount() );
        jackpot.setCurrentPoolAmount( entity.getCurrentPoolAmount() );
        jackpot.setContributionRate( entity.getContributionRate() );
        jackpot.setContributionDecayRate( entity.getContributionDecayRate() );
        jackpot.setRewardChance( entity.getRewardChance() );
        jackpot.setRewardGrowthRate( entity.getRewardGrowthRate() );
        jackpot.setRewardPoolLimit( entity.getRewardPoolLimit() );
        jackpot.setVersion( entity.getVersion() );

        return jackpot;
    }
}

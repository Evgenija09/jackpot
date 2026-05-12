package com.sporty.jackpot.infrastructure.persistence.mapper;

import com.sporty.jackpot.domain.model.JackpotReward;
import com.sporty.jackpot.infrastructure.persistence.entity.JackpotRewardEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-12T09:17:53+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.10 (Microsoft)"
)
@Component
public class JackpotRewardDomainMapperImpl implements JackpotRewardDomainMapper {

    @Override
    public JackpotRewardEntity toEntity(JackpotReward reward) {
        if ( reward == null ) {
            return null;
        }

        JackpotRewardEntity jackpotRewardEntity = new JackpotRewardEntity();

        jackpotRewardEntity.setId( reward.getId() );
        jackpotRewardEntity.setBetId( reward.getBetId() );
        jackpotRewardEntity.setUserId( reward.getUserId() );
        jackpotRewardEntity.setJackpotId( reward.getJackpotId() );
        jackpotRewardEntity.setRewardAmount( reward.getRewardAmount() );
        jackpotRewardEntity.setCreatedAt( reward.getCreatedAt() );

        return jackpotRewardEntity;
    }
}

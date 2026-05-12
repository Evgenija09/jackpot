package com.sporty.jackpot.infrastructure.persistence.mapper;

import com.sporty.jackpot.domain.model.JackpotContribution;
import com.sporty.jackpot.infrastructure.persistence.entity.JackpotContributionEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-12T09:17:53+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.10 (Microsoft)"
)
@Component
public class JackpotContributionDomainMapperImpl implements JackpotContributionDomainMapper {

    @Override
    public JackpotContributionEntity toEntity(JackpotContribution contribution) {
        if ( contribution == null ) {
            return null;
        }

        JackpotContributionEntity jackpotContributionEntity = new JackpotContributionEntity();

        jackpotContributionEntity.setId( contribution.getId() );
        jackpotContributionEntity.setBetId( contribution.getBetId() );
        jackpotContributionEntity.setUserId( contribution.getUserId() );
        jackpotContributionEntity.setJackpotId( contribution.getJackpotId() );
        jackpotContributionEntity.setStakeAmount( contribution.getStakeAmount() );
        jackpotContributionEntity.setContributionAmount( contribution.getContributionAmount() );
        jackpotContributionEntity.setCurrentJackpotAmount( contribution.getCurrentJackpotAmount() );
        jackpotContributionEntity.setCreatedAt( contribution.getCreatedAt() );

        return jackpotContributionEntity;
    }
}

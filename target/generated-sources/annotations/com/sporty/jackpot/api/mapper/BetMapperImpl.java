package com.sporty.jackpot.api.mapper;

import com.sporty.jackpot.api.dto.request.BetPublishRequest;
import com.sporty.jackpot.domain.model.Bet;
import com.sporty.jackpot.domain.model.Money;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-11T21:43:31+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.10 (Microsoft)"
)
@Component
public class BetMapperImpl implements BetMapper {

    @Override
    public Bet toDomain(BetPublishRequest request) {
        if ( request == null ) {
            return null;
        }

        String betId = null;
        String userId = null;
        String jackpotId = null;

        betId = request.getBetId();
        userId = request.getUserId();
        jackpotId = request.getJackpotId();

        Money amount = new Money(request.getAmount());

        Bet bet = new Bet( betId, userId, jackpotId, amount );

        return bet;
    }
}

package com.sporty.jackpot.domain.service;

import com.sporty.jackpot.api.dto.response.BetPublishResponse;
import com.sporty.jackpot.domain.model.Bet;
import org.springframework.stereotype.Service;

@Service
public class BetService {

    private final BetProducer betProducer;

    public BetService(BetProducer betProducer) {
        this.betProducer = betProducer;
    }

    public BetPublishResponse publishBet(Bet bet) {
        betProducer.publish(bet);
        return new BetPublishResponse(bet.getBetId(), "ACCEPTED");
    }
}

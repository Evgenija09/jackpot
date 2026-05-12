package com.sporty.jackpot.domain.service;

import com.sporty.jackpot.domain.model.Bet;

public interface BetProducer {
    void publish(Bet bet);
}

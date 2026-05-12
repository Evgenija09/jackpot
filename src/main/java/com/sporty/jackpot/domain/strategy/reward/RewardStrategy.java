package com.sporty.jackpot.domain.strategy.reward;

import java.math.BigDecimal;

public interface RewardStrategy {

    boolean evaluate(BigDecimal currentPoolAmount, BigDecimal poolLimit);
}

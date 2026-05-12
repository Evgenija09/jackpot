package com.sporty.jackpot.domain.strategy.reward;

import java.math.BigDecimal;
import java.util.Random;

public class FixedRewardStrategy implements RewardStrategy {

    private final BigDecimal winChance;
    private final Random random;

    public FixedRewardStrategy(BigDecimal winChance, Random random) {
        this.winChance = winChance;
        this.random = random;
    }

    @Override
    public boolean evaluate(BigDecimal currentPoolAmount, BigDecimal poolLimit) {
        if (poolLimit != null && currentPoolAmount.compareTo(poolLimit) >= 0) {
            return true;
        }
        return random.nextDouble() < winChance.doubleValue();
    }


    public BigDecimal getWinChance() {
        return winChance;
    }
}

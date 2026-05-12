package com.sporty.jackpot.domain.strategy.reward;

import java.math.BigDecimal;
import java.util.Random;

public class VariableRewardStrategy implements RewardStrategy {

    private static final BigDecimal MAX_CHANCE = BigDecimal.ONE;

    private final BigDecimal baseChance;
    private final BigDecimal growthRate;
    private final BigDecimal poolLimit;
    private final Random random;

    public VariableRewardStrategy(BigDecimal baseChance, BigDecimal growthRate,
                                   BigDecimal poolLimit, Random random) {
        this.baseChance = baseChance;
        this.growthRate = growthRate;
        this.poolLimit = poolLimit;
        this.random = random;
    }

    @Override
    public boolean evaluate(BigDecimal currentPoolAmount, BigDecimal poolLimit) {
        if (poolLimit != null && currentPoolAmount.compareTo(poolLimit) >= 0) {
            return true;
        }
        BigDecimal effectiveChance = getChanceForPool(currentPoolAmount);
        return random.nextDouble() < effectiveChance.doubleValue();
    }


    public BigDecimal getChanceForPool(BigDecimal poolAmount) {
        BigDecimal effectiveChance = baseChance.add(growthRate.multiply(poolAmount));
        if (effectiveChance.compareTo(MAX_CHANCE) > 0) {
            effectiveChance = MAX_CHANCE;
        }
        return effectiveChance;
    }
}

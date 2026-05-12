package com.sporty.jackpot.domain.strategy.contribution;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class VariableContributionStrategy implements ContributionStrategy {

    private static final BigDecimal MINIMUM_RATE = new BigDecimal("0.0100");

    private final BigDecimal baseRate;
    private final BigDecimal decayRate;

    public  VariableContributionStrategy(BigDecimal baseRate, BigDecimal decayRate) {
        this.baseRate = baseRate;
        this.decayRate = decayRate;
    }

    @Override
    public BigDecimal calculate(BigDecimal betAmount, BigDecimal currentPoolAmount) {
        BigDecimal effectiveRate = baseRate.subtract(decayRate.multiply(currentPoolAmount));
        if (effectiveRate.compareTo(MINIMUM_RATE) < 0) {
            effectiveRate = MINIMUM_RATE;
        }
        return betAmount.multiply(effectiveRate).setScale(4, RoundingMode.HALF_UP);
    }
}

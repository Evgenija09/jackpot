package com.sporty.jackpot.domain.strategy.contribution;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FixedContributionStrategy implements ContributionStrategy {

    private final BigDecimal rate;

    public FixedContributionStrategy(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public BigDecimal calculate(BigDecimal betAmount, BigDecimal currentPoolAmount) {
        return betAmount.multiply(rate).setScale(4, RoundingMode.HALF_UP);
    }
}

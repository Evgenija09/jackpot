package com.sporty.jackpot.domain.strategy.contribution;

import java.math.BigDecimal;

public interface ContributionStrategy {

    BigDecimal calculate(BigDecimal betAmount, BigDecimal currentPoolAmount);
}

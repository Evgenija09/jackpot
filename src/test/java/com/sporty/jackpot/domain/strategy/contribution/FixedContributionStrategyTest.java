package com.sporty.jackpot.domain.strategy.contribution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FixedContributionStrategyTest {

    private FixedContributionStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new FixedContributionStrategy(new BigDecimal("0.10"));
    }

    @Test
    void calculate_returnsFixedPercentageOfBetAmount() {
        BigDecimal result = strategy.calculate(new BigDecimal("100.00"), new BigDecimal("1000.00"));

        assertEquals(new BigDecimal("10.0000"), result);
    }

    @Test
    void calculate_poolSizeDoesNotAffectResult() {
        BigDecimal smallPool = strategy.calculate(new BigDecimal("100.00"), new BigDecimal("100.00"));
        BigDecimal largePool = strategy.calculate(new BigDecimal("100.00"), new BigDecimal("999999.00"));

        assertEquals(0, smallPool.compareTo(largePool));
    }

    @Test
    void calculate_withZeroBetAmount_returnsZero() {
        BigDecimal result = strategy.calculate(new BigDecimal("0.00"), new BigDecimal("1000.00"));

        assertEquals(new BigDecimal("0.0000"), result);
    }
}

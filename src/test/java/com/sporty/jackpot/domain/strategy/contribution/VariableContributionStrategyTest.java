package com.sporty.jackpot.domain.strategy.contribution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VariableContributionStrategyTest {

    private VariableContributionStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new VariableContributionStrategy(
                new BigDecimal("0.10"),
                new BigDecimal("0.00001")
        );
    }

    @Test
    void calculate_atZeroPool_usesBaseRate() {
        BigDecimal result = strategy.calculate(new BigDecimal("100.00"), new BigDecimal("0.00"));

        assertEquals(new BigDecimal("10.0000"), result);
    }

    @Test
    void calculate_asPoolGrows_contributionDecreases() {
        BigDecimal smallPoolResult = strategy.calculate(new BigDecimal("100.00"), new BigDecimal("100.00"));
        BigDecimal largePoolResult = strategy.calculate(new BigDecimal("100.00"), new BigDecimal("5000.00"));

        assertTrue(smallPoolResult.compareTo(largePoolResult) > 0);
    }

    @Test
    void calculate_rateFloorsAtMinimum() {
        // effectiveRate = 0.10 - (0.00001 × 99999999) → very negative → floors at 0.0100
        // result = 100.00 × 0.0100 = 1.0000
        BigDecimal result = strategy.calculate(new BigDecimal("100.00"), new BigDecimal("99999999.00"));

        assertEquals(new BigDecimal("1.0000"), result);
    }
}

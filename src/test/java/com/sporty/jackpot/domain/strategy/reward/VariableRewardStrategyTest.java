package com.sporty.jackpot.domain.strategy.reward;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VariableRewardStrategyTest {

    private VariableRewardStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new VariableRewardStrategy(
                new BigDecimal("0.01"),
                new BigDecimal("0.00001"),
                new BigDecimal("10000.00"),
                new Random(42)
        );
    }

    @Test
    void evaluate_atPoolLimit_alwaysWins() {
        boolean result = strategy.evaluate(new BigDecimal("10000.00"), new BigDecimal("10000.00"));

        assertTrue(result);
    }

    @Test
    void evaluate_abovePoolLimit_alwaysWins() {
        boolean result = strategy.evaluate(new BigDecimal("15000.00"), new BigDecimal("10000.00"));

        assertTrue(result);
    }

    @Test
    void evaluate_atZeroPool_usesBaseChance() {
        BigDecimal chance = strategy.getChanceForPool(new BigDecimal("0.00"));

        assertEquals(0, new BigDecimal("0.0100").compareTo(chance));
    }

    @Test
    void evaluate_asPoolGrows_chanceIncreases() {
        BigDecimal smallPoolChance = strategy.getChanceForPool(new BigDecimal("1000"));
        BigDecimal largePoolChance = strategy.getChanceForPool(new BigDecimal("8000"));

        assertTrue(smallPoolChance.compareTo(largePoolChance) < 0);
    }
}

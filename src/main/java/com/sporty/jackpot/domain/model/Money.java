package com.sporty.jackpot.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record Money(@JsonProperty("amount") BigDecimal amount) {

    public Money {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Amount must be non-negative");
    }

    public Money add(Money other) {
        return new Money(amount.add(other.amount));
    }

    public Money multiply(BigDecimal factor) {
        return new Money(amount.multiply(factor));
    }

    @Override
    public String toString() {
        return amount.toPlainString();
    }
}

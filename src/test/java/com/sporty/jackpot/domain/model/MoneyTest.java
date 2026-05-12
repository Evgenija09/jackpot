package com.sporty.jackpot.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyTest {

    @Test
    void constructor_withNegativeAmount_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Money(new BigDecimal("-0.01")));
    }

    @Test
    void constructor_withNullAmount_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Money(null));
    }

    @Test
    void add_returnsSumAsNewMoney() {
        Money a = new Money(new BigDecimal("10"));
        Money b = new Money(new BigDecimal("5"));

        Money result = a.add(b);

        assertEquals(0, new BigDecimal("15").compareTo(result.amount()));
    }

    @Test
    void multiply_returnsProductAsNewMoney() {
        Money money = new Money(new BigDecimal("10"));

        Money result = money.multiply(new BigDecimal("3"));

        assertEquals(0, new BigDecimal("30").compareTo(result.amount()));
    }
}

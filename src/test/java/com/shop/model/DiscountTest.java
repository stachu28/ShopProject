package com.shop.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class DiscountTest {

    @Test
    @DisplayName("Constructor sets all fields")
    void constructorSetsAllFields() {
        Discount discount = new Discount(new BigDecimal("0.10"), "Order over 500");

        assertEquals(new BigDecimal("0.10"), discount.discountRate());
        assertEquals("Order over 500", discount.reason());
    }

    @Test
    @DisplayName("Constructor sets Null Pointer Exception for fields")
    void constructorFieldsRejectNull() {
        assertThrows(NullPointerException.class, () -> new Discount(null, "Test"));
        assertThrows(NullPointerException.class, () -> new Discount(new BigDecimal("0.50"), null));
    }

    @Test
    @DisplayName("Constructor rejects  discount below 0 and over 100%")
    void discountValueRejectsBadDiscount() {
        assertThrows(IllegalArgumentException.class, () -> new Discount(new BigDecimal("1.01"), "Test"));
        assertThrows(IllegalArgumentException.class, () -> new Discount(new BigDecimal("-0.01"), "Test"));
    }

    @Test
    @DisplayName("Constructor accepts boundry rates of 0 and 1")
    void constructorAcceptsBoundryRates() {
        assertDoesNotThrow(() -> new Discount(BigDecimal.ZERO, "Test"));
        assertDoesNotThrow(() -> new Discount(BigDecimal.ONE, "Test"));
    }

    @Test
    @DisplayName("Constructor rejects blank reason")
    void constructorRejectsBlankReason() {
        assertThrows(IllegalArgumentException.class,
                () -> new Discount(new BigDecimal("0.10"), ""));
        assertThrows(IllegalArgumentException.class,
                () -> new Discount(new BigDecimal("0.10"), "   "));
    }

    @Test
    @DisplayName("Discounts with same data are equal")
    void equality() {
        Discount a = new Discount(new BigDecimal("0.10"), "Reason");
        Discount b = new Discount(new BigDecimal("0.10"), "Reason");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
package com.shop.service;

import com.shop.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DiscountStrategyTest {

    private static final Customer CUSTOMER = new Customer(
            "Anna Nowak", "anna@example.com",
            new Address("Szeroka 1", "Warszawa", "02-222", "Poland"));

    private static Order orderWithTotal(String total) {
        Product p = new Electronics(1L, "X", new BigDecimal(total), 10);
        return new Order(CUSTOMER, Map.of(p, 1),
                ZonedDateTime.of(2026, 10, 23, 12, 0, 0, 0, Order.SHOP_ZONE));
    }

    @Test
    @DisplayName("returns zero below 5000")
    void belowFirstThreshold() {
        Discount d = DiscountStrategy.ORDER_VALUE.calculate(orderWithTotal("4999.99"));
        assertEquals(0, BigDecimal.ZERO.compareTo(d.discountRate()));
    }

    @Test
    @DisplayName("returns 10% at exactly 5000")
    void atFirstThreshold() {
        Discount d = DiscountStrategy.ORDER_VALUE.calculate(orderWithTotal("5000.00"));
        assertEquals(0, new BigDecimal("0.10").compareTo(d.discountRate()));
        assertEquals("Order over 5000", d.reason());
    }

    @Test
    @DisplayName("returns 10% between 5000 and 10000")
    void betweenThresholds() {
        Discount d = DiscountStrategy.ORDER_VALUE.calculate(orderWithTotal("9999.99"));
        assertEquals(0, new BigDecimal("0.10").compareTo(d.discountRate()));
    }

    @Test
    @DisplayName("returns 20% at exactly 10000")
    void atSecondThreshold() {
        Discount d = DiscountStrategy.ORDER_VALUE.calculate(orderWithTotal("10000.00"));
        assertEquals(0, new BigDecimal("0.20").compareTo(d.discountRate()));
        assertEquals("Order over 10000", d.reason());
    }

    @Test
    @DisplayName("returns 20% above 10000")
    void aboveSecondThreshold() {
        Discount d = DiscountStrategy.ORDER_VALUE.calculate(orderWithTotal("50000.00"));
        assertEquals(0, new BigDecimal("0.20").compareTo(d.discountRate()));
    }
}
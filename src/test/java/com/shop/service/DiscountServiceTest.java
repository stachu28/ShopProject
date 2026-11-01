package com.shop.service;

import com.shop.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DiscountServiceTest {

    private static final Customer CUSTOMER = new Customer(
            "Krzysztof Nowak", "krzysztof@example.com",
            new Address("Mazowiecka 1", "Warszawa", "02-200", "Poland"));
    private static final LocalDateTime DATE = LocalDateTime.of(2026, 10, 28, 12, 0);

    private DiscountService discountService;

    @BeforeEach
    void setUp() {
        discountService = new DiscountService();
        discountService.setEnabled(true);
    }

    private static Order orderWithTotal(String total) {
        Product p = new Electronics(1L, "X", new BigDecimal(total), 10);
        return new Order(CUSTOMER, Map.of(p, 1), DATE);
    }

    @Test
    @DisplayName("Discounts are disabled by default")
    void disabledByDefault() {
        assertFalse(new DiscountService().isEnabled());
    }

    @Test
    @DisplayName("New service gives no discount until enabled")
    void newServiceGivesNoDiscount() {
        Discount discount = new DiscountService().calculateDiscount(orderWithTotal("12000.00"));

        assertEquals(0, BigDecimal.ZERO.compareTo(discount.discountRate()));
        assertEquals("No discount", discount.reason());
    }

    @Test
    @DisplayName("calculateDiscount throws Null Pointer Exception for null order")
    void calculateDiscountRejectsNull() {
        assertThrows(NullPointerException.class, () -> discountService.calculateDiscount(null));
    }

    @Test
    @DisplayName("No discount below 5000")
    void noDiscountBelowFirstThreshold() {
        Discount discount = discountService.calculateDiscount(orderWithTotal("4999.99"));

        assertEquals(0, BigDecimal.ZERO.compareTo(discount.discountRate()));
        assertEquals("No discount", discount.reason());
    }
}
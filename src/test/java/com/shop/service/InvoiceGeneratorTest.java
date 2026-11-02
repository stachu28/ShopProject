package com.shop.service;

import com.shop.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceGeneratorTest {

    private static final Customer CUSTOMER = new Customer(
            "Anna Nowak", "anna@example.com",
            new Address("Długa 1", "Płock", "09-400", "Poland"));

    private InvoiceGenerator invoiceGenerator;

    @BeforeEach
    void setUp() {
        invoiceGenerator = new InvoiceGenerator(new DiscountService());
    }

    private static Order testOrder(ZonedDateTime date) {
        Product p = new Electronics(1L, "X", new BigDecimal("10.00"), 10);
        return new Order(CUSTOMER, Map.of(p, 1), date);
    }

    @Test
    @DisplayName("Throws Null Pointer Exception for null order")
    void generateRejectsNull() {
        assertThrows(NullPointerException.class, () -> invoiceGenerator.generate(null));
    }

    @Test
    @DisplayName("Returned invoice references the same order")
    void invoiceReferencesOrder() {
        Order order = testOrder(ZonedDateTime.of(2026, 10, 21, 12, 30, 0, 0, ZoneId.of("Europe/Warsaw")));
        Invoice invoice = invoiceGenerator.generate(order);

        assertSame(order, invoice.order());
    }

    @Test
    @DisplayName("Number contains year taken from order date")
    void numberContainsYear() {
        Invoice invoice = invoiceGenerator.generate(testOrder(ZonedDateTime.of(2026, 10, 21, 12, 30, 0, 0,
                ZoneId.of("Europe/Warsaw"))));

        assertTrue(invoice.number().contains("2026"),
                "Number should contain year: " + invoice.number());
    }

    @Test
    @DisplayName("Consecutive calls produce unique numbers")
    void consecutiveCallsProduceUniqueNumbers() {
        Order order = testOrder(ZonedDateTime.of(2026, 10, 23, 12, 0, 0, 0, Order.SHOP_ZONE));

        Invoice a = invoiceGenerator.generate(order);
        Invoice b = invoiceGenerator.generate(order);
        Invoice c = invoiceGenerator.generate(order);

        assertNotEquals(a.number(), b.number());
        assertNotEquals(b.number(), c.number());
        assertNotEquals(a.number(), c.number());
    }
}
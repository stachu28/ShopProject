package com.shop.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceTest {

    private static final Customer CUSTOMER = new Customer(
            "Anna Nowak", "anna@example.com",
            new Address("Poznańska 47", "Warszawa", "02-200", "Poland"));

    private static final LocalDateTime DATE = LocalDateTime.of(2026, 10, 23, 12, 0);
    private static final Discount NO_DISCOUNT = new Discount(BigDecimal.ZERO, "No discount");


    private static Order orderWithTotal(String total) {
        Product p = new Electronics(1L, "X", new BigDecimal(total), 10);
        return new Order(CUSTOMER, Map.of(p, 1), DATE);
    }

    @Test
    @DisplayName("Constructor stores number and order")
    void constructorStoresFields() {
        Order order = orderWithTotal("100.00");
        Invoice invoice = new Invoice("INV/2026/00001", order, NO_DISCOUNT);

        assertEquals("INV/2026/00001", invoice.number());
        assertSame(order, invoice.order());
    }

    @Test
    @DisplayName("Constructor throws Null Pointer Exception for null fields")
    void constructorRejectsNullNumber() {
        Order order = orderWithTotal("100.00");

        assertThrows(NullPointerException.class, () -> new Invoice(null, order, NO_DISCOUNT));
        assertThrows(NullPointerException.class, () -> new Invoice("INV/2026/00001", null, NO_DISCOUNT));
        assertThrows(NullPointerException.class, () -> new Invoice("INV/2026/00001", order, null));
    }

    @Test
    @DisplayName("Constructor rejects empty or blank number")
    void constructorRejectsBlankNumber() {
        Order order = orderWithTotal("100.00");

        assertThrows(IllegalArgumentException.class, () -> new Invoice("", order, NO_DISCOUNT));
        assertThrows(IllegalArgumentException.class, () -> new Invoice("   ", order, NO_DISCOUNT));
    }

    @Test
    @DisplayName("netAmount equals order total")
    void netAmountEqualsOrderTotal() {
        Order order = orderWithTotal("100.00");
        Invoice invoice = new Invoice("INV/1", order, NO_DISCOUNT);

        assertEquals(new BigDecimal("100.00"), invoice.netAmount());
    }

    @Test
    @DisplayName("vatAmount is net times VAT rate rounded HALF_UP to 2 decimals")
    void vatAmountRoundsHalfUp() {
        Invoice invoice = new Invoice("INV/1", orderWithTotal("100.00"), NO_DISCOUNT);
        assertEquals(new BigDecimal("23.00"), invoice.vatAmount());

        Invoice invoice2 = new Invoice("INV/2", orderWithTotal("100.05"), NO_DISCOUNT);
        assertEquals(new BigDecimal("23.01"), invoice2.vatAmount());
    }

    @Test
    @DisplayName("grossAmount equals net plus vat")
    void grossAmountEqualsNetPlusVat() {
        Invoice invoice = new Invoice("INV/1", orderWithTotal("100.00"), NO_DISCOUNT);

        assertEquals(
                invoice.netAmount().add(invoice.vatAmount()),
                invoice.grossAmount());
        assertEquals(new BigDecimal("123.00"), invoice.grossAmount());
    }

    @Test
    @DisplayName("grossAmount, netAmount, vatAmount have scale 2")
    void amountsHaveScaleTwo() {
        Invoice invoice = new Invoice("INV/1", orderWithTotal("100.00"), NO_DISCOUNT);

        assertEquals(2, invoice.netAmount().scale());
        assertEquals(2, invoice.vatAmount().scale());
        assertEquals(2, invoice.grossAmount().scale());
    }
}
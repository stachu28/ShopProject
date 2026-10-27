package com.shop.service;

import com.shop.exceptions.OutOfStockException;
import com.shop.exceptions.ProductNotFoundException;
import com.shop.model.*;
import com.shop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderProcessorTest {

    private static final Customer CUSTOMER = new Customer(
            "Anna Nowak", "anna@example.com",
            new Address("Długa 1", "Płock", "09-400", "Poland"));

    private static final LocalDateTime DATE = LocalDateTime.of(2026, 10, 23, 12, 0);

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InvoiceGenerator invoiceGenerator;

    private OrderProcessor orderProcessor;

    @BeforeEach
    void setUp() {
        orderProcessor = new OrderProcessor(productRepository, invoiceGenerator);
    }

    private static Order orderWith(Map<Product, Integer> items) {
        BigDecimal total = items.entrySet().stream()
                .map(e -> e.getKey().getPrice().multiply(BigDecimal.valueOf(e.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Order(CUSTOMER, items, total, DATE);
    }


    @Test
    @DisplayName("Constructor throws Null Pointer Exception for null repository")
    void constructorRejectsNullRepository() {
        assertThrows(NullPointerException.class,
                () -> new OrderProcessor(null, invoiceGenerator));
    }

    @Test
    @DisplayName("Constructor throws Null Pointer Exception for null generator")
    void constructorRejectsNullGenerator() {
        assertThrows(NullPointerException.class,
                () -> new OrderProcessor(productRepository, null));
    }

    @Test
    @DisplayName("processOrder throws Null Pointer Exception for null order")
    void processRejectsNullOrder() {
        assertThrows(NullPointerException.class, () -> orderProcessor.processOrder(null));
        verifyNoInteractions(productRepository, invoiceGenerator);
    }

    @Test
    @DisplayName("processOrder decreases stock, saves product and returns generated invoice")
    void processDecreasesStockAndReturnsInvoice() {
        Product product = new Electronics(1L, "Headphones", new BigDecimal("100.00"), 10);
        Order order = orderWith(Map.of(product, 3));
        Invoice expected = new Invoice("INV/2026/00001", order);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(invoiceGenerator.generate(order)).thenReturn(expected);

        Invoice result = orderProcessor.processOrder(order);

        assertSame(expected, result);
        assertEquals(7, product.getQuantity());
        verify(productRepository).save(product);
        verify(invoiceGenerator).generate(order);
    }

    @Test
    @DisplayName("processOrder throws ProductNotFoundException")
    void processThrowsWhenProductNotFound() {
        Product product = new Electronics(1L, "X", new BigDecimal("10.00"), 10);
        Order order = orderWith(Map.of(product, 1));

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> orderProcessor.processOrder(order));
    }

    @Test
    @DisplayName("processOrder throws OutOfStockException")
    void processThrowsWhenStockInsufficient() {
        Product product = new Electronics(1L, "X", new BigDecimal("10.00"), 2);
        Order order = orderWith(Map.of(product, 5));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(OutOfStockException.class,
                () -> orderProcessor.processOrder(order));
    }
}
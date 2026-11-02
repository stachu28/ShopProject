package com.shop.service;

import com.shop.exceptions.OutOfStockException;
import com.shop.exceptions.ProductNotFoundException;
import com.shop.model.*;
import com.shop.repository.OrderRepository;
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
    private static final Discount NO_DISCOUNT = new Discount(BigDecimal.ZERO, "No discount");

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InvoiceGenerator invoiceGenerator;

    @Mock
    private OrderRepository orderRepository;

    private OrderProcessor orderProcessor;

    @BeforeEach
    void setUp() {
        orderProcessor = new OrderProcessor(productRepository, invoiceGenerator, orderRepository);
    }

    private static Order orderWith(Map<Product, Integer> items) {
        return new Order(CUSTOMER, items, DATE);
    }


    @Test
    @DisplayName("Constructor throws Null Pointer Exception for null product repository")
    void constructorRejectsNullProductRepository() {
        assertThrows(NullPointerException.class,
                () -> new OrderProcessor(null, invoiceGenerator, orderRepository));
    }

    @Test
    @DisplayName("Constructor throws Null Pointer Exception for null generator")
    void constructorRejectsNullGenerator() {
        assertThrows(NullPointerException.class,
                () -> new OrderProcessor(productRepository, null, orderRepository));
    }

    @Test
    @DisplayName("Constructor throws Null Pointer Exception for null order repository")
    void constructorRejectsNullOrderRepository() {
        assertThrows(NullPointerException.class,
                () -> new OrderProcessor(productRepository, invoiceGenerator, null));
    }

    @Test
    @DisplayName("processOrder throws Null Pointer Exception for null order")
    void processRejectsNullOrder() {
        assertThrows(NullPointerException.class, () -> orderProcessor.processOrder(null));
        verifyNoInteractions(productRepository, invoiceGenerator, orderRepository);
    }

    @Test
    @DisplayName("processOrder decreases stock, saves product and returns generated invoice")
    void processDecreasesStockAndReturnsInvoice() {
        Product product = new Electronics(1L, "Headphones", new BigDecimal("100.00"), 10);
        Order order = orderWith(Map.of(product, 3));
        Invoice expected = new Invoice("INV/2026/00001", order, NO_DISCOUNT);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(invoiceGenerator.generate(order)).thenReturn(expected);

        Invoice result = orderProcessor.processOrder(order);

        assertSame(expected, result);
        assertEquals(7, product.getQuantity());
        verify(productRepository).save(product);
        verify(invoiceGenerator).generate(order);
        verify(orderRepository).save(order);
    }

    @Test
    @DisplayName("processOrder throws ProductNotFoundException and does not save order")
    void processThrowsWhenProductNotFound() {
        Product product = new Electronics(1L, "X", new BigDecimal("10.00"), 10);
        Order order = orderWith(Map.of(product, 1));

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> orderProcessor.processOrder(order));
        verifyNoInteractions(orderRepository, invoiceGenerator);
    }

    @Test
    @DisplayName("processOrder throws OutOfStockException and does not save order")
    void processThrowsWhenStockInsufficient() {
        Product product = new Electronics(1L, "X", new BigDecimal("10.00"), 2);
        Order order = orderWith(Map.of(product, 5));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(OutOfStockException.class,
                () -> orderProcessor.processOrder(order));
        verifyNoInteractions(orderRepository, invoiceGenerator);
    }

    @Test
    @DisplayName("processOrder does not save order when invoice generation fails")
    void processDoesNotSaveOrderWhenInvoiceFails() {
        Product product = new Electronics(1L, "X", new BigDecimal("10.00"), 10);
        Order order = orderWith(Map.of(product, 1));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(invoiceGenerator.generate(order)).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, () -> orderProcessor.processOrder(order));
        verifyNoInteractions(orderRepository);
        assertEquals(10, product.getQuantity(), "stock must not be decreased");
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("processOrder does not decrease stock when order save fails")
    void processDoesNotDecreaseStockWhenSaveFails() {
        Product product = new Electronics(1L, "X", new BigDecimal("10.00"), 10);
        Order order = orderWith(Map.of(product, 1));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(invoiceGenerator.generate(order)).thenReturn(new Invoice("INV/1", order, NO_DISCOUNT));
        doThrow(new RuntimeException("db down")).when(orderRepository).save(order);

        assertThrows(RuntimeException.class, () -> orderProcessor.processOrder(order));
        assertEquals(10, product.getQuantity(), "stock must not be decreased");
        verify(productRepository, never()).save(any());
    }
}
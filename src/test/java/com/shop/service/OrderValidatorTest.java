package com.shop.service;

import com.shop.exceptions.OutOfStockException;
import com.shop.exceptions.ProductNotFoundException;
import com.shop.model.Address;
import com.shop.model.Customer;
import com.shop.model.Electronics;
import com.shop.model.Order;
import com.shop.model.Product;
import com.shop.repository.InMemoryProductRepository;
import com.shop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderValidatorTest {

    private static final Customer CUSTOMER = new Customer(
            "Anna Nowak", "anna@example.com",
            new Address("Długa 1", "Płock", "09-400", "Poland"));

    private ProductRepository repository;
    private Product product;
    private OrderValidator validator;

    @BeforeEach
    void setUp() {
        repository = new InMemoryProductRepository();
        product = new Electronics(1L, "X", new BigDecimal("10.00"), 5);
        repository.save(product);
        validator = OrderValidator.forStock(repository);
    }

    private Order orderOf(int quantity) {
        return Order.builder().customer(CUSTOMER).item(product, quantity).build();
    }

    private static OrderValidator recording(String name, List<String> calls, boolean fails) {
        return new OrderValidator() {
            @Override
            protected void check(Order order) {
                calls.add(name);
                if (fails) {
                    throw new IllegalStateException(name);
                }
            }
        };
    }

    @Test
    @DisplayName("passes when every product exists and is in stock")
    void passesValidOrder() {
        assertDoesNotThrow(() -> validator.validate(orderOf(5)));
    }

    @Test
    @DisplayName("throws ProductNotFoundException when product is not in repository")
    void rejectsMissingProduct() {
        repository.deleteById(1L);

        assertThrows(ProductNotFoundException.class, () -> validator.validate(orderOf(1)));
    }

    @Test
    @DisplayName("throws OutOfStockException when stock is too low")
    void rejectsInsufficientStock() {
        assertThrows(OutOfStockException.class, () -> validator.validate(orderOf(6)));
    }

    @Test
    @DisplayName("handlers run in the order they were chained")
    void runsHandlersInOrder() {
        List<String> calls = new ArrayList<>();

        recording("a", calls, false)
                .setNext(recording("b", calls, false))
                .validate(orderOf(1));

        assertEquals(List.of("a", "b"), calls);
    }

    @Test
    @DisplayName("failing handler stops the chain")
    void stopsAtFirstFailure() {
        List<String> calls = new ArrayList<>();
        OrderValidator chain = recording("a", calls, true).setNext(recording("b", calls, false));

        assertThrows(IllegalStateException.class, () -> chain.validate(orderOf(1)));
        assertEquals(List.of("a"), calls);
    }

    @Test
    @DisplayName("validate rejects null order")
    void rejectsNullOrder() {
        assertThrows(NullPointerException.class, () -> validator.validate(null));
    }
}
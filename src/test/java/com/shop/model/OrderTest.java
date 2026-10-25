package com.shop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Customer customer;
    private Product headphones;
    private Product computer;
    private LocalDateTime orderDate;

    @BeforeEach
    void setUp() {
        customer = new Customer("Anna Nowak", "anna@example.com", new Address("Długa 1", "Płock", "09-400", "Poland"));
        headphones = new Electronics(1L, "Headphones", new BigDecimal("100.00"), 10);
        computer = new Computer(2L, "Laptop", new BigDecimal("5000.00"), 5, "M3", 16, 512);
        orderDate = LocalDateTime.of(2026, 10, 21, 12, 30);
    }

    private Map<Product, Integer> items() {
        Map<Product, Integer> items = new HashMap<>();
        items.put(headphones, 2);
        items.put(computer, 1);
        return items;
    }

    @Test
    @DisplayName("Constructor sets all fields")
    void constructorSetsAllFields() {
        Order order = new Order(customer, items(), new BigDecimal("5200.00"), orderDate);

        assertEquals(customer, order.getCustomer());
        assertEquals(2, order.getItems().size());
        assertEquals(new BigDecimal("5200.00"), order.getTotalPrice());
        assertEquals(orderDate, order.getOrderDate());
    }

    @Test
    @DisplayName("Constructor rejects null customer")
    void constructorRejectsNullCustomer() {
        assertThrows(NullPointerException.class,
                () -> new Order(null, items(), new BigDecimal("5200.00"), orderDate));
    }

    @Test
    @DisplayName("Constructor rejects null items")
    void constructorRejectsNullItems() {
        assertThrows(NullPointerException.class,
                () -> new Order(customer, null, new BigDecimal("5200.00"), orderDate));
    }

    @Test
    @DisplayName("Constructor rejects null total price")
    void constructorRejectsNullTotalPrice() {
        assertThrows(NullPointerException.class,
                () -> new Order(customer, items(), null, orderDate));
    }

    @Test
    @DisplayName("Constructor rejects null order date")
    void constructorRejectsNullOrderDate() {
        assertThrows(NullPointerException.class,
                () -> new Order(customer, items(), new BigDecimal("5200.00"), null));
    }

    @Test
    @DisplayName("Constructor rejects empty items")
    void constructorRejectsEmptyItems() {
        assertThrows(IllegalArgumentException.class,
                () -> new Order(customer, Map.of(), new BigDecimal("0.00"), orderDate));
    }

    @Test
    @DisplayName("Constructor rejects negative total price")
    void constructorRejectsNegativeTotalPrice() {
        assertThrows(IllegalArgumentException.class,
                () -> new Order(customer, items(), new BigDecimal("-1.00"), orderDate));
    }

    @Test
    @DisplayName("Constructor copies items to be immutable")
    void constructorCopiesItems() {
        Map<Product, Integer> mutable = new HashMap<>();
        mutable.put(headphones, 2);

        Order order = new Order(customer, mutable, new BigDecimal("200.00"), orderDate);
        mutable.put(computer, 1);

        assertEquals(1, order.getItems().size());
        assertThrows(UnsupportedOperationException.class,
                () -> order.getItems().put(computer, 1));
    }

    @Test
    @DisplayName("Constructor scales total price to 2 decimal places")
    void constructorScalesTotalPrice() {
        Order order = new Order(customer, items(), new BigDecimal("5200.005"), orderDate);

        assertEquals(new BigDecimal("5200.01"), order.getTotalPrice());
    }
}
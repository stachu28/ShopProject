package com.shop.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderBuilderTest {

    private static final Customer CUSTOMER = new Customer(
            "Anna Nowak", "anna@example.com",
            new Address("Szeroka 1", "Warszawa", "02-222", "Poland"));

    private static Product headphones() {
        return new Electronics(1L, "Headphones", new BigDecimal("100.00"), 10);
    }

    private static Product laptop() {
        return new Computer(2L, "Laptop", new BigDecimal("5000.00"), 5, "M3", 16, 512);
    }

    @Test
    @DisplayName("build creates order with given customer, items and date")
    void buildCreatesOrder() {
        ZonedDateTime date = ZonedDateTime.of(2026, 10, 23, 12, 0, 0, 0, Order.SHOP_ZONE);

        Order order = Order.builder()
                .customer(CUSTOMER)
                .item(headphones(), 2)
                .item(laptop(), 1)
                .orderDate(date)
                .build();

        assertEquals(CUSTOMER, order.getCustomer());
        assertEquals(2, order.getItems().size());
        assertEquals(new BigDecimal("5200.00"), order.getTotalPrice());
        assertEquals(date, order.getOrderDate());
    }

    @Test
    @DisplayName("default orderDate is now in SHOP_ZONE")
    void defaultDateIsNow() {
        ZonedDateTime before = ZonedDateTime.now(Order.SHOP_ZONE).minusSeconds(1);

        Order order = Order.builder()
                .customer(CUSTOMER)
                .item(headphones(), 1)
                .build();

        assertTrue(order.getOrderDate().isAfter(before));
        assertEquals(Order.SHOP_ZONE, order.getOrderDate().getZone());
    }

    @Test
    @DisplayName("adding same product twice sums quantity")
    void duplicateItemsAreSummed() {
        Order order = Order.builder()
                .customer(CUSTOMER)
                .item(headphones(), 2)
                .item(headphones(), 3)
                .build();

        assertEquals(1, order.getItems().size());
        assertEquals(5, order.getItems().get(headphones()));
    }

    @Test
    @DisplayName("items(map) adds all entries")
    void itemsFromMap() {
        Product headphones = headphones();
        Product laptop = laptop();
        Map<Product, Integer> items = new LinkedHashMap<>();
        items.put(headphones, 2);
        items.put(laptop, 1);

        Order order = Order.builder()
                .customer(CUSTOMER)
                .items(items)
                .build();

        assertEquals(2, order.getItems().size());
        assertEquals(2, order.getItems().get(headphones));
        assertEquals(1, order.getItems().get(laptop));
        assertEquals(new BigDecimal("5200.00"), order.getTotalPrice());
    }

    @Test
    @DisplayName("items(map) validates each entry")
    void itemsFromMapValidatesEntries() {
        Map<Product, Integer> invalid = Map.of(headphones(), 0);

        assertThrows(IllegalArgumentException.class,
                () -> Order.builder().customer(CUSTOMER).items(invalid));
    }

    @Test
    @DisplayName("items(map) rejects null map")
    void itemsRejectsNullMap() {
        assertThrows(NullPointerException.class,
                () -> Order.builder().items(null));
    }

    @Test
    @DisplayName("build throws when customer is missing")
    void buildRejectsMissingCustomer() {
        OrderBuilder builder = Order.builder().item(headphones(), 1);
        assertThrows(NullPointerException.class, builder::build);
    }

    @Test
    @DisplayName("build throws when no items")
    void buildRejectsEmptyItems() {
        OrderBuilder builder = Order.builder().customer(CUSTOMER);
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    @DisplayName("item rejects null product")
    void itemRejectsNullProduct() {
        assertThrows(NullPointerException.class,
                () -> Order.builder().item(null, 1));
    }

    @Test
    @DisplayName("item rejects non-positive quantity")
    void itemRejectsNonPositiveQuantity() {
        assertThrows(IllegalArgumentException.class,
                () -> Order.builder().item(headphones(), 0));
        assertThrows(IllegalArgumentException.class,
                () -> Order.builder().item(headphones(), -1));
    }

    @Test
    @DisplayName("customer rejects null")
    void customerRejectsNull() {
        assertThrows(NullPointerException.class,
                () -> Order.builder().customer(null));
    }

    @Test
    @DisplayName("orderDate rejects null")
    void orderDateRejectsNull() {
        assertThrows(NullPointerException.class,
                () -> Order.builder().orderDate(null));
    }

    @Test
    @DisplayName("builder accumulates state between builds")
    void builderAccumulates() {
        OrderBuilder builder = Order.builder().customer(CUSTOMER);
        Order a = builder.item(headphones(), 1).build();
        Order b = builder.item(laptop(), 1).build();

        assertEquals(1, a.getItems().size());
        assertEquals(2, b.getItems().size());
    }
}
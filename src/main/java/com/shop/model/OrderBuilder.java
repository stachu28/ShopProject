package com.shop.model;

import lombok.NonNull;

import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Builder design-pattern implementation for Order Class
 */
public final class OrderBuilder {

    private Customer customer;
    private final Map<Product, Integer> items = new LinkedHashMap<>();
    private ZonedDateTime orderDate = ZonedDateTime.now(Order.SHOP_ZONE);

    public OrderBuilder customer(@NonNull Customer customer) {
        this.customer = customer;
        return this;
    }

    public OrderBuilder item(@NonNull Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive: " + quantity);
        }
        items.merge(product, quantity, Integer::sum);
        return this;
    }

    public OrderBuilder items(@NonNull Map<Product, Integer> items) {
        items.forEach(this::item);
        return this;
    }

    public OrderBuilder orderDate(@NonNull ZonedDateTime orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public Order build() {
        Objects.requireNonNull(customer, "Customer must be set before build()");
        if (items.isEmpty()) {
            throw new IllegalStateException("Order must contain at least one item");
        }
        return new Order(customer, items, orderDate);
    }
}
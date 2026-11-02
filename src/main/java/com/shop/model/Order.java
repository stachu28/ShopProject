package com.shop.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

@Getter
@ToString
public class Order {

    @NonNull
    private final Customer customer;
    @NonNull
    private final Map<Product, Integer> items;
    @NonNull
    private final BigDecimal totalPrice;
    @NonNull
    private final ZonedDateTime orderDate;

    public static final ZoneId SHOP_ZONE = ZoneId.of("Europe/Warsaw");

    public Order(@NonNull Customer customer,
                 @NonNull Map<Product, Integer> items,
                 @NonNull ZonedDateTime orderDate) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Order cannot be empty");
        }
        this.customer = customer;
        this.items = Map.copyOf(items);
        this.totalPrice = items.entrySet().stream()
                .map(e -> e.getKey().getPrice().multiply(BigDecimal.valueOf(e.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        this.orderDate = orderDate;
    }
}
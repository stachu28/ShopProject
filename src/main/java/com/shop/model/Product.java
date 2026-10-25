package com.shop.model;

import java.math.BigDecimal;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Product {

    @EqualsAndHashCode.Include
    @NonNull
    private final Long id;

    @Setter
    @NonNull
    private String name;

    @NonNull
    private BigDecimal price;

    private int quantity;

    public Product(@NonNull Long id, @NonNull String name, @NonNull BigDecimal price, int quantity) {
        this.id = id;
        this.name = name;
        setPrice(price);
        setQuantity(quantity);
    }

    public abstract ProductType getType();

    public final void setPrice(@NonNull BigDecimal price) {
        if (price.signum() < 0) {
            throw new IllegalArgumentException("Price cannot be negative: " + price);
        }
        this.price = price;
    }

    public final void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative: " + quantity);
        }
        this.quantity = quantity;
    }
}
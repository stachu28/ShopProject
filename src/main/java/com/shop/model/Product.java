package com.shop.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.*;

/**
 * Base class for all products sold in the shop.
 */
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

    /** Returns the category of the product (computer, smartphone, etc.). */
    public abstract ProductType getType();

    /** Sets the price rounded to 2 decimals; negative values are rejected. */
    public final void setPrice(@NonNull BigDecimal price) {
        if (price.signum() < 0) {
            throw new IllegalArgumentException("Price cannot be negative: " + price);
        }
        this.price = price.setScale(2, RoundingMode.HALF_UP);
    }

    public final void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative: " + quantity);
        }
        this.quantity = quantity;
    }
}
package com.shop.model;

import java.math.BigDecimal;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
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

    public abstract ProductType getType();

    public final void setPrice(@NonNull BigDecimal price) {
        if (price.signum() < 0) {
            throw new IllegalArgumentException("Cena nie może być ujemna: " + price);
        }
        this.price = price;
    }

    public final void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Ilość nie może być ujemna: " + quantity);
        }
        this.quantity = quantity;
    }
}
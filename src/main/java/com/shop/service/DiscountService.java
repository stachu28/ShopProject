package com.shop.service;

import com.shop.model.Discount;
import com.shop.model.Order;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Calculates discounts for orders. Discounts are off by default.
 */
public class DiscountService {
    
    @Getter
    @Setter
    private boolean enabled = false;

    @Setter
    @NonNull
    private DiscountStrategy strategy = DiscountStrategy.ORDER_VALUE;

    /**
     * Returns 20% for orders from 10000, 10% from 5000, otherwise no discount (always none when disabled).
     */
    public Discount calculateDiscount(Order order) {
        Objects.requireNonNull(order, "Order cannot be null");
        if (!enabled) {
            return new Discount(BigDecimal.ZERO, "No discount");
        }
        return strategy.calculate(order);
    }
}
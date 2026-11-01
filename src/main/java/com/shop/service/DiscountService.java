package com.shop.service;

import com.shop.model.Discount;
import com.shop.model.Order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class DiscountService {

    private boolean enabled = false;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Discount calculateDiscount(Order order) {
        Objects.requireNonNull(order, "Order cannot be null");
        BigDecimal total = order.getTotalPrice();

        if (enabled && total.compareTo(new BigDecimal("10000")) >= 0) {
            return new Discount(new BigDecimal("0.20"), "Order over 10000");
        }
        if (enabled && total.compareTo(new BigDecimal("5000")) >= 0) {
            return new Discount(new BigDecimal("0.10"), "Order over 5000");
        }
        return new Discount(BigDecimal.ZERO, "No discount");
    }

    public BigDecimal calculateDiscountAmount(Order order) {
        Discount discount = calculateDiscount(order);
        return order.getTotalPrice()
                .multiply(discount.discountRate())
                .setScale(2, RoundingMode.HALF_UP);
    }
}
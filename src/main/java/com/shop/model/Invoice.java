package com.shop.model;

import lombok.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Invoice(@NonNull String number, @NonNull Order order, @NonNull Discount discount) {

    public static final BigDecimal VAT_RATE = new BigDecimal("0.23");

    public Invoice {
        if (number.isBlank()) {
            throw new IllegalArgumentException("Invoice number cannot be blank");
        }
    }

    public BigDecimal discountAmount() {
        return order.getTotalPrice()
                .multiply(discount.discountRate())
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal netAmount() {
        return order.getTotalPrice().subtract(discountAmount());
    }

    public BigDecimal vatAmount() {
        return netAmount().multiply(VAT_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal grossAmount() {
        return netAmount().add(vatAmount());
    }
}
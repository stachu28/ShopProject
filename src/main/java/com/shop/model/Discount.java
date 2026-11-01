package com.shop.model;

import lombok.NonNull;

import java.math.BigDecimal;

public record Discount(@NonNull BigDecimal discountRate, @NonNull String reason) {

    public Discount {
        if (discountRate.compareTo(BigDecimal.ZERO) < 0 || discountRate.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("Rate must be between 0 and 1: " + discountRate);
        }
        if (reason.isBlank()) {
            throw new IllegalArgumentException("Reason cannot be blank");
        }
    }
}
package com.shop.model;

import com.shop.config.Settings;
import lombok.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Invoice for an order, with the discount applied before VAT.
 */
public record Invoice(@NonNull String number, @NonNull Order order, @NonNull Discount discount) {

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

    /** Order total minus the discount (before VAT). */
    public BigDecimal netAmount() {
        return order.getTotalPrice().subtract(discountAmount());
    }

    public BigDecimal vatAmount() {
        return netAmount().multiply(Settings.INSTANCE.getVatRate()).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal grossAmount() {
        return netAmount().add(vatAmount());
    }
}
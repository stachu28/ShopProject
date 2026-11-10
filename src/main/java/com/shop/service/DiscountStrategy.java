package com.shop.service;

import com.shop.config.Settings;
import com.shop.model.Discount;
import com.shop.model.Order;

import java.math.BigDecimal;

/**
 * Discount calculation strategy*
 */
public enum DiscountStrategy {

    ORDER_VALUE {
        @Override
        public Discount calculate(Order order) {
            BigDecimal total = order.getTotalPrice();
            Settings settings = Settings.INSTANCE;

            if (total.compareTo(settings.getBigOrderLimit()) >= 0) {
                return new Discount(new BigDecimal("0.20"), "Order over 10000");
            }
            if (total.compareTo(settings.getOrderLimit()) >= 0) {
                return new Discount(new BigDecimal("0.10"), "Order over 5000");
            }
            return new Discount(BigDecimal.ZERO, "No discount");
        }
    };

    public abstract Discount calculate(Order order);
}
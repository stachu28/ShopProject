package com.shop.config;

import com.shop.model.Order;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.ZoneId;

@Getter
public enum Settings {

    INSTANCE;

    private final String shopName = "Sklep";
    private final BigDecimal orderLimit = new BigDecimal("5000.00");
    private final BigDecimal manualApprovalThreshold = new BigDecimal("2000.00");
    private final BigDecimal freeDeliveryThreshold = new BigDecimal("200.00");
    private final BigDecimal vatRate = new BigDecimal("0.23");

    public static final ZoneId SHOP_ZONE = Order.SHOP_ZONE;
}
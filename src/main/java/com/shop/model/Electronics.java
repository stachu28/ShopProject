package com.shop.model;

import java.math.BigDecimal;

import lombok.ToString;

/**
 * General electronic device, e.g. headphones or a keyboard.
 */
@ToString(callSuper = true)
public class Electronics extends Product {

    public Electronics(Long id, String name, BigDecimal price, int quantity) {
        super(id, name, price, quantity);
    }

    @Override
    public ProductType getType() {
        return ProductType.ELECTRONICS;
    }
}
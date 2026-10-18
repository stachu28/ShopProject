package com.shop.model;

import java.math.BigDecimal;

import lombok.ToString;

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
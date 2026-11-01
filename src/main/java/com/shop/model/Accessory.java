package com.shop.model;

import lombok.ToString;

import java.math.BigDecimal;

@ToString(callSuper = true)
public class Accessory extends Product {

    public Accessory(Long id, String name, BigDecimal price, int quantity) {
        super(id, name, price, quantity);
    }

    @Override
    public ProductType getType() {
        return ProductType.ACCESSORY;
    }
}
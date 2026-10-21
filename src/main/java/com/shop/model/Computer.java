package com.shop.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString(callSuper = true)
public class Computer extends Product {

    @NonNull
    private final String processor;
    private final int ramGb;
    private final int storageGb;

    public Computer(Long id, String name, BigDecimal price, int quantity,
                    @NonNull String processor, int ramGb, int storageGb) {
        super(id, name, price, quantity);
        if (ramGb < 0) throw new IllegalArgumentException("RAM size cannot be negative: " + ramGb);
        if (storageGb < 0) throw new IllegalArgumentException("Memory drive cannot be negative: " + storageGb);
        this.processor = processor;
        this.ramGb = ramGb;
        this.storageGb = storageGb;
    }

    @Override
    public ProductType getType() {
        return ProductType.COMPUTER;
    }
}
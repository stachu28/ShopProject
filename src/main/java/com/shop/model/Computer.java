package com.shop.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString(callSuper = true)
public class Computer extends Product {

    @NonNull
    private String processor;
    private int ramGb;
    private int storageGb;

    public Computer(Long id, String name, BigDecimal price, int quantity,
                    @NonNull String processor, int ramGb, int storageGb) {
        super(id, name, price, quantity);
        setProcessor(processor);
        setRamGb(ramGb);
        setStorageGb(storageGb);
    }

    public void setProcessor(@NonNull String processor) {
        if (processor.isBlank()) {
            throw new IllegalArgumentException("Processor cannot be blank");
        }
        this.processor = processor;
    }

    public void setRamGb(int ramGb) {
        if (ramGb < 0) {
            throw new IllegalArgumentException("RAM size cannot be negative: " + ramGb);
        }
        this.ramGb = ramGb;
    }

    public void setStorageGb(int storageGb) {
        if (storageGb < 0) {
            throw new IllegalArgumentException("Memory drive cannot be negative: " + storageGb);
        }
        this.storageGb = storageGb;
    }

    @Override
    public ProductType getType() {
        return ProductType.COMPUTER;
    }
}
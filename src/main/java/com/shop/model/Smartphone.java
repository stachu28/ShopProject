package com.shop.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class Smartphone extends Product {

    @NonNull
    private final String color;
    private final int batteryCapacityMah;
    private final List<Product> accessories = new ArrayList<>();

    public Smartphone(Long id, String name, BigDecimal price, int quantity,
                      @NonNull String color, int batteryCapacityMah) {
        super(id, name, price, quantity);
        if (batteryCapacityMah < 0) {
            throw new IllegalArgumentException("Battery capacity cannot be negative: " + batteryCapacityMah);
        }
        this.color = color;
        this.batteryCapacityMah = batteryCapacityMah;
    }

    public void addAccessory(@NonNull Product accessory) {
        if (accessory.getType() != ProductType.ACCESSORY) {
            throw new IllegalArgumentException("Only ACCESSORY products can be added, got: " + accessory.getType());
        }
        accessories.add(accessory);
    }

    @Override
    public ProductType getType() {
        return ProductType.SMARTPHONE;
    }
}
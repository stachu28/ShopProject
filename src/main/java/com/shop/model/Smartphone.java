package com.shop.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class Smartphone extends Product {

    @NonNull
    private String color;
    private int batteryCapacityMah;
    private List<String> accessories = new ArrayList<>();

    public Smartphone(Long id, String name, BigDecimal price, int quantity,
                      String color, int batteryCapacityMah) {
        super(id, name, price, quantity);
        this.color = color;
        this.batteryCapacityMah = batteryCapacityMah;
    }

    public void addAccessory(String accessory) {
        accessories.add(accessory);
    }

    @Override
    public ProductType getType() {
        return ProductType.SMARTPHONE;
    }
}
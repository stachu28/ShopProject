package com.shop.model;

import lombok.NonNull;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {

    private final Map<Product, Integer> cartContent = new LinkedHashMap<>();

    public void addItem(@NonNull Product product, @NonNull Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity cannot be negative: " + quantity);
        }
        cartContent.merge(product, quantity, Integer::sum);
    }

    public void removeItem(@NonNull Product product) {
        cartContent.remove(product);
    }

    public void clear() {
        cartContent.clear();
    }

    public boolean isEmpty() {
        return cartContent.isEmpty();
    }

    public int size() {
        return cartContent.size();
    }

    public int totalQuantity() {
        return cartContent.values().stream().mapToInt(Integer::intValue).sum();
    }

    public BigDecimal totalPrice() {
        return cartContent.entrySet().stream()
                .map(e -> e.getKey().getPrice().multiply(BigDecimal.valueOf(e.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, java.math.RoundingMode.HALF_UP);
    }

    public Map<Product, Integer> getItems() {
        return Map.copyOf(cartContent);
    }
}
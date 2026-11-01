package com.shop.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ElectronicsTest {

    @Test
    @DisplayName("getType returns 'Electronics'")
    void getTypeReturnsElectronics() {
        Electronics e = new Electronics(1L, "USB-C charger", new BigDecimal("29.99"), 100);
        assertEquals(ProductType.ELECTRONICS, e.getType());
    }
}
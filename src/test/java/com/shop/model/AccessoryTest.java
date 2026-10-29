package com.shop.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccessoryTest {

    @Test
    @DisplayName("getType returns 'Accessory'")
    void getTypeReturnsAccessory() {
        Accessory accessory = new Accessory(1L, "Charger", new BigDecimal("49.00"), 18);
        assertEquals(ProductType.ACCESSORY, accessory.getType());
    }

    @Test
    @DisplayName("Inherits negative price validation from Product")
    void rejectsNegativePrice() {
        assertThrows(IllegalArgumentException.class,
                () -> new Accessory(1L, "Charger", new BigDecimal("-1"), 1));
    }
}
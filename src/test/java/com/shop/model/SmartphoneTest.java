package com.shop.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SmartphoneTest {

    private static Smartphone createSmartphone() {
        return new Smartphone(1L, "Galaxy S24", new BigDecimal("3999.00"), 25, "Black", 4000);
    }

    @Test
    @DisplayName("getType returns smartphone")
    void getTypeReturnsSmartphone() {
        assertEquals(ProductType.SMARTPHONE, createSmartphone().getType());
    }

    @Test
    @DisplayName("Constructor throws Null Pointer Exception in null color")
    void constructorRejectsNullColor() {
        assertThrows(NullPointerException.class,
                () -> new Smartphone(1L, "Galaxy S24", new BigDecimal("3999.00"), 25, null, 4000));
    }

    @Test
    @DisplayName("addAccessory adds accessories")
    void addAccessoryAddsAccessories() {
        Smartphone phone = createSmartphone();

        phone.addAccessory("Charger");
        phone.addAccessory("Case");

        assertEquals(List.of("Charger", "Case"), phone.getAccessories());
    }

    @Test
    @DisplayName("Constructor rejects negative battery capacity")
    void constructorRejectsNegativeBattery() {
        assertThrows(IllegalArgumentException.class,
                () -> new Smartphone(1L, "Galaxy S24", new BigDecimal("3999.00"), 25, "Black", -1));
    }

    @Test
    @DisplayName("addAccessory throws Null Pointer Exception in null accessory")
    void addAccessoryRejectsNull() {
        Smartphone phone = createSmartphone();
        assertThrows(NullPointerException.class, () -> phone.addAccessory(null));
        assertTrue(phone.getAccessories().isEmpty());
    }
}
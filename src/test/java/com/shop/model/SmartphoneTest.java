package com.shop.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SmartphoneTest {

    private static Smartphone createSmartphone() {
        return new Smartphone(1L, "Galaxy S24", new BigDecimal("3999.00"), 25, "Czarny", 4000);
    }

    @Test
    @DisplayName("getType zwraca smartphone")
    void getTypeReturnsSmartphone() {
        assertEquals(ProductType.SMARTPHONE, createSmartphone().getType());
    }

    @Test
    @DisplayName("Konstruktor rzuca NPE w kolorze")
    void constructorRejectsNullColor() {
        assertThrows(NullPointerException.class,
                () -> new Smartphone(1L, "Galaxy S24", new BigDecimal("3999.00"), 25, null, 4000));
    }

    @Test
    @DisplayName("addAccessory dodaje akcesoria")
    void addAccessoryAddsAccessories() {
        Smartphone phone = createSmartphone();

        phone.addAccessory("Ładowarka");
        phone.addAccessory("Etui");

        assertEquals(List.of("Ładowarka", "Etui"), phone.getAccessories());
    }
}
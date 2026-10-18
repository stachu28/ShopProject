package com.shop.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ComputerTest {

    @Test
    @DisplayName("getType zwraca 'Computer'")
    void getTypeReturnsComputer() {
        Computer computer = new Computer(1L, "MacBook Pro", new BigDecimal("8999.00"), 3, "M3 Silicon", 16, 512);

        assertEquals(ProductType.COMPUTER, computer.getType());
    }

    @Test
    @DisplayName("Konstruktor odrzuca null w nazwie procesora")
    void constructorRejectsNullInRamName() {
        assertThrows(NullPointerException.class,
                () -> new Computer(1L, "MacBook Pro", new BigDecimal("8999.00"), 3, null, 16, 512));
    }
}
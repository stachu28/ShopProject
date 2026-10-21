package com.shop.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ComputerTest {

    @Test
    @DisplayName("getType returns 'Computer'")
    void getTypeReturnsComputer() {
        Computer computer = new Computer(1L, "MacBook Pro", new BigDecimal("8999.00"), 3, "M3 Silicon", 16, 512);

        assertEquals(ProductType.COMPUTER, computer.getType());
    }

    @Test
    @DisplayName("Constructor throws Null Pointer Exception for null processor")
    void constructorRejectsNullProcessor() {
        assertThrows(NullPointerException.class,
                () -> new Computer(1L, "MacBook Pro", new BigDecimal("8999.00"), 3, null, 16, 512));
    }

    @Test
    @DisplayName("Constructor rejects negative RAM")
    void constructorRejectsNegativeRam() {
        assertThrows(IllegalArgumentException.class,
                () -> new Computer(1L, "MacBook Pro", new BigDecimal("8999.00"), 3, "M3", -1, 512));
    }

    @Test
    @DisplayName("Constructor rejects negative storage")
    void constructorRejectsNegativeStorage() {
        assertThrows(IllegalArgumentException.class,
                () -> new Computer(1L, "MacBook Pro", new BigDecimal("8999.00"), 3, "M3", 16, -1));
    }
}
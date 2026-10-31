package com.shop.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    @Test
    @DisplayName("Constructor sets all fields")
    void constructorSetsAllFields() {
        Address address = new Address("Długa 1", "Płock", "09-400", "Poland");

        assertEquals("Długa 1", address.street());
        assertEquals("Płock", address.city());
        assertEquals("09-400", address.postalCode());
        assertEquals("Poland", address.country());
    }

    @Test
    @DisplayName("Constructor throws Null Pointer Exception for null fields")
    void constructorRejectsNullFields() {
        assertThrows(NullPointerException.class, () -> new Address(null, "Płock", "09-400", "Poland"));
        assertThrows(NullPointerException.class, () -> new Address("Długa 1", null, "09-400", "Poland"));
        assertThrows(NullPointerException.class, () -> new Address("Długa 1", "Płock", null, "Poland"));
        assertThrows(NullPointerException.class, () -> new Address("Długa 1", "Płock", "09-400", null));
    }

    @Test
    @DisplayName("Constructor rejects blank fields")
    void constructorRejectsBlankFields() {
        assertThrows(IllegalArgumentException.class, () -> new Address(" ", "Płock", "09-400", "Poland"));
        assertThrows(IllegalArgumentException.class, () -> new Address("Długa 1", " ", "09-400", "Poland"));
        assertThrows(IllegalArgumentException.class, () -> new Address("Długa 1", "Płock", " ", "Poland"));
        assertThrows(IllegalArgumentException.class, () -> new Address("Długa 1", "Płock", "09-400", " "));
    }

    @Test
    @DisplayName("Addresses with same data are equal")
    void equality() {
        Address a = new Address("Długa 1", "Płock", "09-400", "Poland");
        Address b = new Address("Długa 1", "Płock", "09-400", "Poland");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
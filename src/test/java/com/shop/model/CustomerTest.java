package com.shop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address("Długa 1", "Płock", "09-400", "Poland");
    }

    @Test
    @DisplayName("Constructor sets all fields")
    void constructorSetsAllFields() {
        Customer customer = new Customer("Anna Nowak", "anna@example.com", address);

        assertEquals("Anna Nowak", customer.name());
        assertEquals("anna@example.com", customer.email());
        assertEquals(address, customer.address());
    }

    @Test
    @DisplayName("Constructor rejects null name")
    void constructorRejectsNullName() {
        assertThrows(NullPointerException.class, () -> new Customer(null, "anna@example.com", address));
    }

    @Test
    @DisplayName("Constructor rejects blank name")
    void constructorRejectsBlankName() {
        assertThrows(IllegalArgumentException.class, () -> new Customer("   ", "anna@example.com", address));
    }

    @Test
    @DisplayName("Constructor rejects null email")
    void constructorRejectsNullEmail() {
        assertThrows(NullPointerException.class, () -> new Customer("Anna", null, address));
    }

    @Test
    @DisplayName("Constructor rejects blank email")
    void constructorRejectsBlankEmail() {
        assertThrows(IllegalArgumentException.class, () -> new Customer("Anna", "   ", address));
    }

    @Test
    @DisplayName("Constructor rejects null address")
    void constructorRejectsNullAddress() {
        assertThrows(NullPointerException.class, () -> new Customer("Anna", "anna@example.com", null));
    }

    @Test
    @DisplayName("Constructor rejects email without @")
    void constructorRejectsEmailWithoutAt() {
        assertThrows(IllegalArgumentException.class, () -> new Customer("Anna", "anna.example.com", address));
    }

    @Test
    @DisplayName("Customers with same data are equal")
    void equality() {
        Customer a = new Customer("Anna", "anna@example.com", address);
        Customer b = new Customer("Anna", "anna@example.com", address);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
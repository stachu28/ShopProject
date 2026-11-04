package com.shop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CartTest {

    private Cart cart;
    private Product headphones;
    private Product computer;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        headphones = new Electronics(1L, "Headphones", new BigDecimal("100.00"), 10);
        computer = new Computer(2L, "Laptop", new BigDecimal("5000.00"), 5, "M3", 16, 512);
    }

    @Test
    @DisplayName("New shopping cart is empty")
    void newCartIsEmpty() {
        assertTrue(cart.isEmpty());
        assertEquals(0, cart.size());
        assertEquals(BigDecimal.ZERO.setScale(2), cart.totalPrice());
    }

    @Test
    @DisplayName("addItem adds item to the cart")
    void addItemAddsProduct() {
        cart.addItem(headphones, 2);

        assertEquals(1, cart.size());
        assertEquals(2, cart.totalQuantity());
        assertEquals(new BigDecimal("200.00"), cart.totalPrice());
    }

    @Test
    @DisplayName("addItem of the same product increases its quantity")
    void addItemMergesQuantity() {
        cart.addItem(headphones, 2);
        cart.addItem(headphones, 3);

        assertEquals(1, cart.size());
        assertEquals(5, cart.totalQuantity());
        assertEquals(new BigDecimal("500.00"), cart.totalPrice());
    }

    @Test
    @DisplayName("addItem throws Null Pointer Exception for null product")
    void addItemRejectsNullProduct() {
        assertThrows(NullPointerException.class, () -> cart.addItem(null, 1));
    }

    @Test
    @DisplayName("addItem throws Null Pointer Exception for null quanitity")
    void addItemRejectsNullQuantity() {
        assertThrows(NullPointerException.class, () -> cart.addItem(headphones, null));
    }

    @Test
    @DisplayName("addItem rejects non-positive quantity")
    void addItemRejectsNonPositiveQuantity() {
        assertThrows(IllegalArgumentException.class, () -> cart.addItem(headphones, 0));
        assertThrows(IllegalArgumentException.class, () -> cart.addItem(headphones, -1));
    }

    @Test
    @DisplayName("removeItem removes an item from cart")
    void removeItemRemovesProduct() {
        cart.addItem(headphones, 2);

        cart.removeItem(headphones);

        assertTrue(cart.isEmpty());
    }

    @Test
    @DisplayName("removeItem ignores missing product")
    void removeItemIgnoresMissing() {
        cart.removeItem(headphones);
        assertTrue(cart.isEmpty());
    }

    @Test
    @DisplayName("removeItem removes only given product")
    void removeItemRemovesOnlyGivenProduct() {
        cart.addItem(headphones, 2);
        cart.addItem(computer, 1);

        cart.removeItem(headphones);

        assertEquals(1, cart.size());
        assertEquals(1, cart.totalQuantity());
        assertEquals(new BigDecimal("5000.00"), cart.totalPrice());
    }

    @Test
    @DisplayName("clear removes all")
    void clearRemovesAll() {
        cart.addItem(headphones, 2);
        cart.addItem(computer, 1);

        cart.clear();

        assertTrue(cart.isEmpty());
        assertEquals(0, cart.size());
        assertEquals(0, cart.totalQuantity());
        assertEquals(new BigDecimal("0.00"), cart.totalPrice());
    }

    @Test
    @DisplayName("totalQuantity sums quantities")
    void totalQuantitySums() {
        cart.addItem(headphones, 2);
        cart.addItem(computer, 1);

        assertEquals(3, cart.totalQuantity());
    }

    @Test
    @DisplayName("totalPrice calculate cart total")
    void totalPriceCalculates() {
        cart.addItem(headphones, 2);
        cart.addItem(computer, 1);

        assertEquals(new BigDecimal("5200.00"), cart.totalPrice());
    }

    @Test
    @DisplayName("totalPrice for empty cart is 0.00")
    void totalPriceEmptyIsZero() {
        assertEquals(new BigDecimal("0.00"), cart.totalPrice());
    }
}
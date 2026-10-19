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
        headphones = new Electronics(1L, "Słuchawki", new BigDecimal("100.00"), 10);
        computer = new Computer(2L, "Laptop", new BigDecimal("5000.00"), 5, "M3", 16, 512);
    }

    @Test
    @DisplayName("Nowy koszyk nie zawiera żadnych produktów")
    void newCartIsEmpty() {
        assertTrue(cart.isEmpty());
        assertEquals(0, cart.size());
        assertEquals(BigDecimal.ZERO.setScale(2), cart.totalPrice());
    }

    @Test
    @DisplayName("addItem dodaje produkt")
    void addItemAddsProduct() {
        cart.addItem(headphones, 2);

        assertEquals(1, cart.size());
        assertEquals(2, cart.totalQuantity());
        assertEquals(new BigDecimal("200.00"), cart.totalPrice());
    }

    @Test
    @DisplayName("addItem tego samego produktu zwiększa ilość")
    void addItemMergesQuantity() {
        cart.addItem(headphones, 2);
        cart.addItem(headphones, 3);

        assertEquals(1, cart.size());
        assertEquals(5, cart.totalQuantity());
        assertEquals(new BigDecimal("500.00"), cart.totalPrice());
    }

    @Test
    @DisplayName("addItem odrzuca null produkt")
    void addItemRejectsNullProduct() {
        assertThrows(NullPointerException.class, () -> cart.addItem(null, 1));
    }

    @Test
    @DisplayName("addItem odrzuca null ilość")
    void addItemRejectsNullQuantity() {
        assertThrows(NullPointerException.class, () -> cart.addItem(headphones, null));
    }

    @Test
    @DisplayName("addItem odrzuca ilość ≤ 0")
    void addItemRejectsNonPositiveQuantity() {
        assertThrows(IllegalArgumentException.class, () -> cart.addItem(headphones, 0));
        assertThrows(IllegalArgumentException.class, () -> cart.addItem(headphones, -1));
    }

    @Test
    @DisplayName("removeItem usuwa produkt")
    void removeItemRemovesProduct() {
        cart.addItem(headphones, 2);

        cart.removeItem(headphones);

        assertTrue(cart.isEmpty());
    }

    @Test
    @DisplayName("removeItem nie rzuca dla nieistniejącego produktu")
    void removeItemIgnoresMissing() {
        cart.removeItem(headphones);
        assertTrue(cart.isEmpty());
    }

    @Test
    @DisplayName("removeItem usuwa tylko wskazany produkt")
    void removeItemRemovesOnlyGivenProduct() {
        cart.addItem(headphones, 2);
        cart.addItem(computer, 1);

        cart.removeItem(headphones);

        assertEquals(1, cart.size());
        assertEquals(1, cart.totalQuantity());
        assertEquals(new BigDecimal("5000.00"), cart.totalPrice());
    }

    @Test
    @DisplayName("clear usuwa wszystko")
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
    @DisplayName("totalQuantity sumuje ilości")
    void totalQuantitySums() {
        cart.addItem(headphones, 2);
        cart.addItem(computer, 1);

        assertEquals(3, cart.totalQuantity());
    }

    @Test
    @DisplayName("totalPrice liczy wartość koszyka")
    void totalPriceCalculates() {
        cart.addItem(headphones, 2);
        cart.addItem(computer, 1);

        assertEquals(new BigDecimal("5200.00"), cart.totalPrice());
    }

    @Test
    @DisplayName("totalPrice dla pustego koszyka to 0.00")
    void totalPriceEmptyIsZero() {
        assertEquals(new BigDecimal("0.00"), cart.totalPrice());
    }
}
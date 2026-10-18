package com.shop.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private static class TestProduct extends Product {

        TestProduct(Long id, String name, BigDecimal price, int quantity) {
            super(id, name, price, quantity);
        }

        @Override
        public ProductType getType() {
            return ProductType.ELECTRONICS;
        }
    }

    private static Product createProduct() {
        return new TestProduct(1L, "Słuchawki", new BigDecimal("100"), 10);
    }

    @Test
    @DisplayName("Konstruktor ustawia wszystkie pola")
    void constructorSetsAllFields() {
        Product product = createProduct();

        assertEquals(1L, product.getId());
        assertEquals("Słuchawki", product.getName());
        assertEquals(new BigDecimal("100"), product.getPrice());
        assertEquals(10, product.getQuantity());
    }

    @Test
    @DisplayName("Konstruktor odrzuca ujemną cenę")
    void constructorRejectNegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> new TestProduct(1L, "Test", new BigDecimal("-1"), 1));
    }

    @Test
    @DisplayName("Konstruktor odrzuca null w id")
    void constructorRejectNullId() {
        assertThrows(NullPointerException.class, () -> new TestProduct(null, "Test", BigDecimal.TEN, 1));
    }

    @Test
    @DisplayName("Konstruktor odrzuca null w nazwie")
    void constructorRejectNullName() {
        assertThrows(NullPointerException.class, () -> new TestProduct(1L, null, BigDecimal.TEN, 1));
    }

    @Test
    @DisplayName("Cena równa zero i ilość równa zero są dozwolone")
    void zeroPriceAndQuantityAreAllowed() {
        Product product = new TestProduct(1L, "Gratis", BigDecimal.ZERO, 0);

        assertEquals(BigDecimal.ZERO, product.getPrice());
        assertEquals(0, product.getQuantity());
    }

    @Test
    @DisplayName("getType zwraca typ zdefiniowany przez podklasę")
    void getTypeIsProvidedBySubclass() {
        assertEquals(ProductType.ELECTRONICS, createProduct().getType());
    }

    @Test
    @DisplayName("setName zmienia nazwę")
    void setNameChangesName() {
        Product product = createProduct();

        product.setName("Nowa nazwa");

        assertEquals("Nowa nazwa", product.getName());
    }

    @Test
    @DisplayName("setName odrzuca null i nie zmienia dotychczasowej nazwy")
    void setNameRejectsNull() {
        Product product = createProduct();

        assertThrows(NullPointerException.class, () -> product.setName(null));
        assertEquals("Słuchawki", product.getName());
    }

    @Test
    @DisplayName("setPrice zmienia cenę")
    void setPriceChangesPrice() {
        Product product = createProduct();

        product.setPrice(new BigDecimal("50.00"));

        assertEquals(new BigDecimal("50.00"), product.getPrice());
    }

    @Test
    @DisplayName("setPrice odrzuca cenę ujemną i nie zmienia dotychczasowej ceny")
    void setPriceRejectsNegative() {
        Product product = createProduct();

        assertThrows(IllegalArgumentException.class, () -> product.setPrice(new BigDecimal("-0.01")));
        assertEquals(new BigDecimal("100"), product.getPrice());
    }

    @Test
    @DisplayName("setPrice odrzuca null")
    void setPriceRejectsNull() {
        Product product = createProduct();

        assertThrows(NullPointerException.class, () -> product.setPrice(null));
        assertEquals(new BigDecimal("100"), product.getPrice());
    }

    @Test
    @DisplayName("setQuantity zmienia ilość, także na zero")
    void setQuantityChangesQuantity() {
        Product product = createProduct();

        product.setQuantity(0);
        assertEquals(0, product.getQuantity());

        product.setQuantity(42);
        assertEquals(42, product.getQuantity());
    }

    @Test
    @DisplayName("setQuantity odrzuca ilość ujemną i nie zmienia dotychczasowej ilości")
    void setQuantityRejectsNegative() {
        Product product = createProduct();

        assertThrows(IllegalArgumentException.class, () -> product.setQuantity(-5));
        assertEquals(10, product.getQuantity());
    }

    @Test
    @DisplayName("Produkty z tym samym id są równe, niezależnie od pozostałych pól")
    void equalsBasedOnId() {
        Product a = new TestProduct(1L, "A", new BigDecimal("10"), 1);
        Product b = new TestProduct(1L, "B", new BigDecimal("999"), 50);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    @DisplayName("Produkty z różnym id nie są równe")
    void notEqualsForDifferentId() {
        Product a = new TestProduct(1L, "A", BigDecimal.TEN, 1);
        Product b = new TestProduct(2L, "A", BigDecimal.TEN, 1);

        assertNotEquals(a, b);
    }
}
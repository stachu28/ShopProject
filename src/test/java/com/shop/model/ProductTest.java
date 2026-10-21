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
        return new TestProduct(1L, "Headphones", new BigDecimal("100"), 10);
    }

    @Test
    @DisplayName("Constructor sets all fields")
    void constructorSetsAllFields() {
        Product product = createProduct();

        assertEquals(1L, product.getId());
        assertEquals("Headphones", product.getName());
        assertEquals(new BigDecimal("100"), product.getPrice());
        assertEquals(10, product.getQuantity());
    }

    @Test
    @DisplayName("Constructor reject negative price")
    void constructorRejectNegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> new TestProduct(1L, "Test", new BigDecimal("-1"), 1));
    }

    @Test
    @DisplayName("Constructor throws Null Pointer Exception in null product id")
    void constructorRejectNullId() {
        assertThrows(NullPointerException.class, () -> new TestProduct(null, "Test", BigDecimal.TEN, 1));
    }

    @Test
    @DisplayName("Constructor throws Null Pointer Exception in null product name")
    void constructorRejectNullName() {
        assertThrows(NullPointerException.class, () -> new TestProduct(1L, null, BigDecimal.TEN, 1));
    }

    @Test
    @DisplayName("Allowing price and quantity to be equal to 0")
    void zeroPriceAndQuantityAreAllowed() {
        Product product = new TestProduct(1L, "Gratis", BigDecimal.ZERO, 0);

        assertEquals(BigDecimal.ZERO, product.getPrice());
        assertEquals(0, product.getQuantity());
    }

    @Test
    @DisplayName("getType returns type provided by subclass")
    void getTypeIsProvidedBySubclass() {
        assertEquals(ProductType.ELECTRONICS, createProduct().getType());
    }

    @Test
    @DisplayName("setName changes name")
    void setNameChangesName() {
        Product product = createProduct();

        product.setName("New name");

        assertEquals("New name", product.getName());
    }

    @Test
    @DisplayName("setName throws Null Pointer Exception and does not implement changes")
    void setNameRejectsNull() {
        Product product = createProduct();

        assertThrows(NullPointerException.class, () -> product.setName(null));
        assertEquals("Headphones", product.getName());
    }

    @Test
    @DisplayName("setPrice changes price")
    void setPriceChangesPrice() {
        Product product = createProduct();

        product.setPrice(new BigDecimal("50.00"));

        assertEquals(new BigDecimal("50.00"), product.getPrice());
    }

    @Test
    @DisplayName("setPrice rejects negative price and does not implement changes")
    void setPriceRejectsNegative() {
        Product product = createProduct();

        assertThrows(IllegalArgumentException.class, () -> product.setPrice(new BigDecimal("-0.01")));
        assertEquals(new BigDecimal("100"), product.getPrice());
    }

    @Test
    @DisplayName("setPrice throws Null Pointer Exception for null price")
    void setPriceRejectsNull() {
        Product product = createProduct();

        assertThrows(NullPointerException.class, () -> product.setPrice(null));
        assertEquals(new BigDecimal("100"), product.getPrice());
    }

    @Test
    @DisplayName("setQuantity changes quantity including changing to quantity 0")
    void setQuantityChangesQuantity() {
        Product product = createProduct();

        product.setQuantity(0);
        assertEquals(0, product.getQuantity());

        product.setQuantity(42);
        assertEquals(42, product.getQuantity());
    }

    @Test
    @DisplayName("setQuantity rejects negative quantity and does not implement changes")
    void setQuantityRejectsNegative() {
        Product product = createProduct();

        assertThrows(IllegalArgumentException.class, () -> product.setQuantity(-5));
        assertEquals(10, product.getQuantity());
    }

    @Test
    @DisplayName("Products are equal based on id")
    void equalsBasedOnId() {
        Product a = new TestProduct(1L, "A", new BigDecimal("10"), 1);
        Product b = new TestProduct(1L, "B", new BigDecimal("999"), 50);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    @DisplayName("Products with different id are not equal")
    void notEqualsForDifferentId() {
        Product a = new TestProduct(1L, "A", BigDecimal.TEN, 1);
        Product b = new TestProduct(2L, "A", BigDecimal.TEN, 1);

        assertNotEquals(a, b);
    }
}
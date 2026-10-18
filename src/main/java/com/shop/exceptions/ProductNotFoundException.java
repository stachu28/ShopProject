package com.shop.exceptions;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("Produkt nie istnieje: " + id);
    }
}
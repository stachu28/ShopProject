package com.shop.exceptions;

/**
 * Thrown when a product with the given id does not exist.
 */
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("Product does not exist: " + id);
    }
}
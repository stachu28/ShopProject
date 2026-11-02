package com.shop.exceptions;

public class OutOfStockException extends RuntimeException {

    public OutOfStockException(Long productId, int available, int requested) {
        super("Product " + productId + " is out of stock: available " + available
                + ", requested " + requested);
    }
}
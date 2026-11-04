package com.shop.exceptions;

/**
 * Thrown when there is not enough stock to fulfil an order.
 */
public class OutOfStockException extends RuntimeException {

    public OutOfStockException(Long productId, int available, int requested) {
        super("Product " + productId + " is out of stock: available " + available
                + ", requested " + requested);
    }
}
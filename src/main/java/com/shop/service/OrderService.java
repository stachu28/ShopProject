package com.shop.service;

import java.util.concurrent.ThreadLocalRandom;

public class OrderService {

    public String createOrder(int orderId) {
        simulateLongOrderProcessing();
        return "Order-" + orderId;
    }

    private void simulateLongOrderProcessing() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(100, 201));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Processing interrupted", e);
        }
    }
}
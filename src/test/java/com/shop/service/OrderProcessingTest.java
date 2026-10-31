package com.shop.service;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderProcessingTest {

    private static final int ORDERS_COUNT = 100;
    private static final int THREADS_COUNT = 10;

    private final OrderService orderService = new OrderService();

    private List<String> processOrdersSync(List<Integer> orderIds) {
        return orderIds.stream()
                .map(orderService::createOrder)
                .toList();
    }

    private List<String> processOrdersAsync(List<Integer> orderIds) {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS_COUNT);
        try {
            List<CompletableFuture<String>> futures = orderIds.stream()
                    .map(id -> CompletableFuture.supplyAsync(() -> orderService.createOrder(id), executor))
                    .toList();
            return futures.stream()
                    .map(CompletableFuture::join)
                    .toList();
        } finally {
            executor.shutdown();
        }
    }

    @Test
    void shouldProcessOrdersFasterAsynchronously() {
        List<Integer> orderIds = IntStream.rangeClosed(1, ORDERS_COUNT).boxed().toList();

        long start = System.nanoTime();
        processOrdersSync(orderIds);
        long syncTime = System.nanoTime() - start;

        start = System.nanoTime();
        List<String> asyncResults = processOrdersAsync(orderIds);
        long asyncTime = System.nanoTime() - start;

        System.out.printf("SYNC:  %.2f s%n", syncTime / 1_000_000_000.0);
        System.out.printf("ASYNC: %.2f s%n", asyncTime / 1_000_000_000.0);

        assertEquals(ORDERS_COUNT, asyncResults.size());
        assertTrue(asyncTime < syncTime);
    }
}
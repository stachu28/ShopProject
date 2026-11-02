package com.shop.service;

import com.shop.model.Address;
import com.shop.model.Customer;
import com.shop.model.Electronics;
import com.shop.model.Invoice;
import com.shop.model.Order;
import com.shop.model.Product;
import com.shop.repository.InMemoryOrderRepository;
import com.shop.repository.InMemoryProductRepository;
import com.shop.repository.ProductRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderProcessingTest {

    private static final int ORDERS_COUNT = 100;

    private static final Customer CUSTOMER = new Customer(
            "Anna Nowak", "anna@example.com",
            new Address("Długa 1", "Warszawa", "02-222", "Poland"));
    private static final LocalDateTime DATE = LocalDateTime.of(2026, 10, 23, 12, 0);

    private static Product newProduct() {
        return new Electronics(1L, "X", new BigDecimal("10.00"), ORDERS_COUNT);
    }

    private static OrderProcessor newProcessor(Product product) {
        ProductRepository repository = new InMemoryProductRepository();
        repository.save(product);
        return new OrderProcessor(repository, new InvoiceGenerator(new DiscountService()),
                new InMemoryOrderRepository());
    }

    private static List<Order> newOrders(Product product) {
        return IntStream.range(0, ORDERS_COUNT)
                .mapToObj(i -> new Order(CUSTOMER, Map.of(product, 1), DATE))
                .toList();
    }

    @Test
    void shouldProcessOrdersFasterAsynchronously() {
        Product syncProduct = newProduct();
        OrderProcessor syncProcessor = newProcessor(syncProduct);
        List<Order> syncOrders = newOrders(syncProduct);

        Product asyncProduct = newProduct();
        OrderProcessor asyncProcessor = newProcessor(asyncProduct);
        List<Order> asyncOrders = newOrders(asyncProduct);

        long start = System.nanoTime();
        List<Invoice> syncInvoices = syncProcessor.processOrdersSync(syncOrders);
        long syncTime = System.nanoTime() - start;

        start = System.nanoTime();
        List<Invoice> asyncInvoices = asyncProcessor.processOrdersAsync(asyncOrders);
        long asyncTime = System.nanoTime() - start;

        System.out.printf("SYNC:  %.2f s%n", syncTime / 1_000_000_000.0);
        System.out.printf("ASYNC: %.2f s%n", asyncTime / 1_000_000_000.0);

        assertEquals(ORDERS_COUNT, syncInvoices.size());
        assertEquals(ORDERS_COUNT, asyncInvoices.size());
        assertEquals(0, syncProduct.getQuantity());
        assertEquals(0, asyncProduct.getQuantity());
        assertTrue(asyncTime < syncTime);
    }
}
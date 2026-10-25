package com.shop.service;

import com.shop.exceptions.OutOfStockException;
import com.shop.exceptions.ProductNotFoundException;
import com.shop.model.Invoice;
import com.shop.model.Order;
import com.shop.model.Product;
import com.shop.repository.ProductRepository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class OrderProcessor {

    private final ProductRepository productRepository;
    private final InvoiceGenerator invoiceGenerator;

    public OrderProcessor(ProductRepository repository, InvoiceGenerator invoice) {
        this.productRepository = Objects.requireNonNull(repository, "Repository cannot be null");
        this.invoiceGenerator = Objects.requireNonNull(invoice, "Invoice Generator cannot be null");
    }

    public Invoice processOrder(Order order) {
        Objects.requireNonNull(order, "Order cannot be null");

        Map<Product, Integer> reservations = reserveStock(order);
        reservations.forEach((product, quantity) -> {
            product.setQuantity(product.getQuantity() - quantity);
            productRepository.save(product);
        });
        return invoiceGenerator.generate(order);
    }

    private Map<Product, Integer> reserveStock(Order order) {
        Map<Product, Integer> reservations = new LinkedHashMap<>();

        order.getItems().forEach((orderedProduct, quantity) -> {
            Long id = orderedProduct.getId();
            Product stored = productRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException(id));
            if (stored.getQuantity() < quantity) {
                throw new OutOfStockException(id, stored.getQuantity(), quantity);
            }
            reservations.put(stored, quantity);
        });
        return reservations;
    }
}
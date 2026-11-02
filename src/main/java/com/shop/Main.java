package com.shop;

import com.shop.cli.ShopCli;
import com.shop.model.*;
import com.shop.repository.InMemoryOrderRepository;
import com.shop.service.DiscountService;
import com.shop.service.ProductManager;
import com.shop.repository.InMemoryProductRepository;
import com.shop.service.InvoiceGenerator;
import com.shop.service.OrderProcessor;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        InMemoryProductRepository productRepository = new InMemoryProductRepository();
        InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();
        ProductManager manager = new ProductManager(productRepository);

        DiscountService discountService = new DiscountService();
        OrderProcessor processor = new OrderProcessor(productRepository, new InvoiceGenerator(discountService), orderRepository);

        seed(manager);

        try (Scanner scanner = new Scanner(System.in)) {
            new ShopCli(scanner, manager, new Cart(), processor, discountService).run();
        }
    }

    private static void seed(ProductManager manager) {
        manager.addProduct(new Computer(1L, "Laptop",      new BigDecimal("7999.00"), 5,  "Intel i7", 16, 512));
        manager.addProduct(new Smartphone(2L, "Smartphone", new BigDecimal("3499.00"), 12, "Black", 4000));
        manager.addProduct(new Electronics(3L, "Headphones", new BigDecimal("299.00"), 30));
        manager.addProduct(new Electronics(4L, "Keyboard",   new BigDecimal("449.00"), 18));
        manager.addProduct(new Accessory(5L, "Charger",   new BigDecimal("49.00"), 18));
    }
}
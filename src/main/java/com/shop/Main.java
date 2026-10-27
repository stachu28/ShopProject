package com.shop;

import com.shop.cli.ShopCli;
import com.shop.model.Cart;
import com.shop.model.Computer;
import com.shop.model.Electronics;
import com.shop.service.ProductManager;
import com.shop.model.Smartphone;
import com.shop.repository.InMemoryProductRepository;
import com.shop.service.InvoiceGenerator;
import com.shop.service.OrderProcessor;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        InMemoryProductRepository repository = new InMemoryProductRepository();
        ProductManager manager = new ProductManager(repository);
        OrderProcessor processor = new OrderProcessor(repository, new InvoiceGenerator());

        seed(manager);

        try (Scanner scanner = new Scanner(System.in)) {
            new ShopCli(scanner, manager, new Cart(), processor).run();
        }
    }

    private static void seed(ProductManager manager) {
        manager.addProduct(new Computer(1L, "Laptop",      new BigDecimal("7999.00"), 5,  "Intel i7", 16, 512));
        manager.addProduct(new Smartphone(2L, "Smartphone", new BigDecimal("3499.00"), 12, "Black", 4000));
        manager.addProduct(new Electronics(3L, "Headphones", new BigDecimal("299.00"), 30));
        manager.addProduct(new Electronics(4L, "Keyboard",   new BigDecimal("449.00"), 18));
    }
}
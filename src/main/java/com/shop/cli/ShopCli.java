package com.shop.cli;

import com.shop.model.Address;
import com.shop.model.Cart;
import com.shop.model.Customer;
import com.shop.model.Invoice;
import com.shop.model.Order;
import com.shop.model.Product;
import com.shop.service.OrderProcessor;
import com.shop.service.ProductManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Scanner;

public class ShopCli {

    private static final Address DEFAULT_ADDRESS =
            new Address("Szeroka 1", "Warszawa", "02-345", "Poland");

    private static final String MENU = """
            
            === SHOP ===
            1. Show products
            2. Add product to cart
            3. Show cart
            4. Place order
            0. Exit
            """;

    private final Scanner scanner;
    private final ProductManager manager;
    private final Cart cart;
    private final OrderProcessor processor;

    public ShopCli(Scanner scanner, ProductManager manager, Cart cart, OrderProcessor processor) {
        this.scanner = scanner;
        this.manager = manager;
        this.cart = cart;
        this.processor = processor;
    }

    public void run() {
        while (true) {
            System.out.print(MENU);
            System.out.print("Choose option: ");
            String choice = readLine();
            try {
                switch (choice) {
                    case "1" -> showProducts();
                    case "2" -> addProductToCart();
                    case "3" -> showCart();
                    case "4" -> checkout();
                    case "0" -> {
                        System.out.println("Bye.");
                        return;
                    }
                    default -> System.out.println("Unknown option: " + choice);
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void showProducts() {
        manager.findAll().forEach(p -> System.out.printf(
                "  [%d] %-16s %10.2f zł  stock: %d%n",
                p.getId(), p.getName(),
                p.getPrice().doubleValue(), p.getQuantity()));
    }

    private void addProductToCart() {
        long id = readLong("Product id: ");
        Product product = manager.findById(id).orElse(null);
        if (product == null) {
            System.out.println("Product with id " + id + " not found");
            return;
        }
        int qty = readInt("Quantity: ");
        cart.addItem(product, qty);
        System.out.println("Added: " + product.getName() + " x" + qty);
    }

    private void showCart() {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        for (Map.Entry<Product, Integer> e : cart.getItems().entrySet()) {
            Product p = e.getKey();
            int qty = e.getValue();
            BigDecimal line = p.getPrice().multiply(BigDecimal.valueOf(qty));
            System.out.printf("  %-16s x%-3d %10.2f zł%n",
                    p.getName(), qty, line.doubleValue());
        }
        System.out.printf("Total: %.2f zł%n", cart.totalPrice().doubleValue());
    }

    private void checkout() {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        String name = readText("Your name:  ");
        String email = readText("Your email: ");
        Customer customer = new Customer(name, email, DEFAULT_ADDRESS);

        Order order = new Order(
                customer,
                cart.getItems(),
                cart.totalPrice(),
                LocalDateTime.now());

        Invoice invoice = processor.processOrder(order);
        cart.clear();

        System.out.printf("%n=== INVOICE %s ===%n", invoice.number());
        System.out.printf("Net:   %.2f zł%n", invoice.netAmount().doubleValue());
        System.out.printf("VAT:   %.2f zł%n", invoice.vatAmount().doubleValue());
        System.out.printf("Total: %.2f zł%n", invoice.grossAmount().doubleValue());
    }

    private String readLine() {
        return scanner.nextLine().trim();
    }

    private String readText(String prompt) {
        System.out.print(prompt);
        return readLine();
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(readLine());
            } catch (NumberFormatException e) {
                System.out.println("Not a number, try again.");
            }
        }
    }

    private long readLong(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Long.parseLong(readLine());
            } catch (NumberFormatException e) {
                System.out.println("Not a number, try again.");
            }
        }
    }
}
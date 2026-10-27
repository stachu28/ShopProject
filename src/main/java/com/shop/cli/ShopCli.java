package com.shop.cli;

import com.shop.model.*;
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

        boolean configured = configureIfNeeded(product);
        if (configured) {
            manager.update(product);
        }

        int qty = readInt("Quantity: ");
        cart.addItem(product, qty);
        System.out.println("Added: " + product.getName() + " x" + qty);
    }

    private boolean configureIfNeeded(Product product) {
        if (product instanceof Computer computer) {
            return configureComputer(computer);
        }
        if (product instanceof Smartphone smartphone) {
            return configureSmartphone(smartphone);
        }
        return false;
    }

    private boolean configureComputer(Computer computer) {
        System.out.println("Configure computer (press Enter to keep current value):");

        System.out.printf("  Processor [%s]: ", computer.getProcessor());
        String proc = readLine();
        if (!proc.isEmpty()) {
            computer.setProcessor(proc);
        }

        Integer ram = readOptionalNonNegativeInt("RAM GB", computer.getRamGb());
        if (ram != null) {
            computer.setRamGb(ram);
        }

        Integer storage = readOptionalNonNegativeInt("Storage GB", computer.getStorageGb());
        if (storage != null) {
            computer.setStorageGb(storage);
        }
        return true;
    }

    private boolean configureSmartphone(Smartphone smartphone) {
        System.out.println("Configure smartphone (press Enter to keep current value):");

        System.out.printf("  Color [%s]: ", smartphone.getColor());
        String color = readLine();
        if (!color.isEmpty()) {
            smartphone.setColor(color);
        }

        Integer battery = readOptionalNonNegativeInt("Battery mAh", smartphone.getBatteryCapacityMah());
        if (battery != null) {
            smartphone.setBatteryCapacityMah(battery);
        }

        while (true) {
            System.out.print("Add accessory by id (empty to finish): ");
            String accId = readLine();
            if (accId.isEmpty()) break;

            long id;
            try {
                id = Long.parseLong(accId);
            } catch (NumberFormatException e) {
                System.out.println("Not a number, try again.");
                continue;
            }

            Product acc = manager.findById(id).orElse(null);
            if (acc == null) {
                System.out.println("Product with id " + id + " not found");
                continue;
            }
            if (acc.getType() != ProductType.ACCESSORY) {
                System.out.println("Product " + id + " is not an accessory");
                continue;
            }
            smartphone.addAccessory(acc);
            System.out.println("Accessory added: " + acc.getName());
        }
        return true;
    }

    private Integer readOptionalNonNegativeInt(String label, int current) {
        System.out.printf("  %s [%d]: ", label, current);
        String s = readLine();
        if (s.isEmpty()) {
            return null;
        }
        try {
            int v = Integer.parseInt(s);
            if (v < 0) {
                System.out.println("Value cannot be negative, keeping current.");
                return null;
            }
            return v;
        } catch (NumberFormatException e) {
            System.out.println("Not a number, keeping current.");
            return null;
        }
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
package com.shop.service;

import com.shop.model.Discount;
import com.shop.model.Invoice;
import com.shop.model.Order;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceGenerator {

    private static final String INVOICE_FORMAT = "INV/%d/%05d";

    private final AtomicLong invoiceCounter = new AtomicLong(0);
    private final DiscountService discountService;

    public InvoiceGenerator(DiscountService discountService) {
        this.discountService = Objects.requireNonNull(discountService, "Discount Service cannot be null");
    }

    public Invoice generate(Order order) {
        Objects.requireNonNull(order, "Order cannot be null");

        String number = String.format(INVOICE_FORMAT,
                order.getOrderDate().getYear(), invoiceCounter.incrementAndGet());

        Discount discount = discountService.calculateDiscount(order);

        return new Invoice(number, order, discount);
    }
}
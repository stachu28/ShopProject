package com.shop.service;

import com.shop.exceptions.OutOfStockException;
import com.shop.exceptions.ProductNotFoundException;
import com.shop.model.Order;
import com.shop.repository.ProductRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Chain of Responsibility: every handler checks one rule and passes the order to the next one.
 */
public abstract class OrderValidator {

    private OrderValidator next;

    public OrderValidator setNext(@NonNull OrderValidator next) {
        this.next = next;
        return this;
    }

    public final void validate(@NonNull Order order) {
        check(order);
        if (next != null) {
            next.validate(order);
        }
    }

    protected abstract void check(Order order);

    /** Chain used by the shop: every product exists, then there is enough stock of each. */
    public static OrderValidator forStock(@NonNull ProductRepository repository) {
        return new ProductExists(repository).setNext(new SufficientStock(repository));
    }

    @RequiredArgsConstructor
    private static final class ProductExists extends OrderValidator {

        @NonNull
        private final ProductRepository repository;

        @Override
        protected void check(Order order) {
            order.getItems().keySet().forEach(product -> {
                if (repository.findById(product.getId()).isEmpty()) {
                    throw new ProductNotFoundException(product.getId());
                }
            });
        }
    }

    @RequiredArgsConstructor
    private static final class SufficientStock extends OrderValidator {

        @NonNull
        private final ProductRepository repository;

        @Override
        protected void check(Order order) {
            order.getItems().forEach((product, quantity) ->
                    repository.findById(product.getId())
                            .filter(stored -> stored.getQuantity() < quantity)
                            .ifPresent(stored -> {
                                throw new OutOfStockException(stored.getId(), stored.getQuantity(), quantity);
                            }));
        }
    }
}
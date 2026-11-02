package com.shop.repository;

import com.shop.model.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class InMemoryOrderRepository implements OrderRepository {

    private final List<Order> storage = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void save(Order order) {
        storage.add(Objects.requireNonNull(order, "Order cannot be null"));
    }

    @Override
    public List<Order> findAll() {
        synchronized (storage) {
            return Collections.unmodifiableList(new ArrayList<>(storage));
        }
    }

    @Override
    public List<Order> findByCustomerEmail(String email) {
        Objects.requireNonNull(email, "Email cannot be null");
        synchronized (storage) {
            List<Order> result = new ArrayList<>();
            for (Order o : storage) {
                if (o.getCustomer().email().equalsIgnoreCase(email)) {
                    result.add(o);
                }
            }
            return Collections.unmodifiableList(result);
        }
    }
}
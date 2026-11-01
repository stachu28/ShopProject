package com.shop.repository;

import com.shop.model.Product;
import com.shop.model.ProductType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryProductRepository implements ProductRepository {

    private final Map<Long, Product> storage = new ConcurrentHashMap<>();

    @Override
    public void save(Product product) {
        storage.put(product.getId(), product);
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Product> findByType(ProductType type) {
        return storage.values().stream().filter(p -> p.getType() == type).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }
}
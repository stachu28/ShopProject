package com.shop.service;

import com.shop.exceptions.ProductNotFoundException;
import com.shop.model.Product;
import com.shop.model.ProductType;
import com.shop.repository.ProductRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ProductManager {

    private final ProductRepository repository;

    public ProductManager(ProductRepository repository) {
        this.repository = Objects.requireNonNull(repository, "Repository cannot be null");
    }

    public void addProduct(Product product) {
        Objects.requireNonNull(product, "Product cannot be null");
        if (repository.existsById(product.getId())) {
            throw new IllegalArgumentException("Product with id: " + product.getId() + " already exists");
        }
        repository.save(product);
    }

    public void removeProduct(Long id) {
        Objects.requireNonNull(id, "Product id cannot be null");
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        repository.deleteById(id);
    }

    public void update(Product updated) {
        Objects.requireNonNull(updated, "Product cannot be null");
        Product existing = repository.findById(updated.getId())
                .orElseThrow(() -> new ProductNotFoundException(updated.getId()));
        if (!existing.getClass().equals(updated.getClass())) {
            throw new IllegalArgumentException("Cannot change product type: "
                    + existing.getClass().getSimpleName()
                    + " → " + updated.getClass().getSimpleName());
        }
        repository.save(updated);
    }

    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }

    public List<Product> findAll() {
        return repository.findAll();
    }

    public List<Product> findByType(ProductType type) {
        Objects.requireNonNull(type, "Type cannot be null");
        return repository.findByType(type);
    }
}
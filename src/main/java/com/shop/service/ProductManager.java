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
        this.repository = Objects.requireNonNull(repository, "Repozytorium nie może być null");
    }

    public void addProduct(Product product) {
        Objects.requireNonNull(product, "Produkt nie może być null");
        if (repository.existsById(product.getId())) {
            throw new IllegalArgumentException("Produkt o id " + product.getId() + " już istnieje");
        }
        repository.save(product);
    }

    public void removeProduct(Long id) {
        Objects.requireNonNull(id, "id produktu nie może być null");
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        repository.deleteById(id);
    }

    public void update(Product updated) {
        Objects.requireNonNull(updated, "Produkt nie może być null");
        Product existing = repository.findById(updated.getId())
                .orElseThrow(() -> new ProductNotFoundException(updated.getId()));
        if (!existing.getClass().equals(updated.getClass())) {
            throw new IllegalArgumentException("Nie można zmienić typu produktu: "
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
        Objects.requireNonNull(type, "Typ nie może być null");
        return repository.findByType(type);
    }
}
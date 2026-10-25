package com.shop.repository;

import com.shop.model.Product;
import com.shop.model.ProductType;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    void save(Product product);

    void deleteById(Long id);

    Optional<Product> findById(Long id);

    List<Product> findAll();

    List<Product> findByType(ProductType type);

    boolean existsById(Long id);
}
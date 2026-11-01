package com.shop.service;

import com.shop.exceptions.ProductNotFoundException;
import com.shop.model.Cart;
import com.shop.model.Product;
import com.shop.repository.ProductRepository;

import java.util.Map;
import java.util.Objects;

public class CartService {

    private final ProductRepository productRepository;

    public CartService(ProductRepository productRepository) {
        this.productRepository = Objects.requireNonNull(
                productRepository, "Repository cannot be null");
    }

    public void addToCart(Cart cart, Long productId, int quantity) {
        Objects.requireNonNull(cart, "Cart cannot be null");
        Objects.requireNonNull(productId, "Product id cannot be null");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        cart.addItem(product, quantity);
    }

    public void removeFromCart(Cart cart, Long productId) {
        Objects.requireNonNull(cart, "Cart cannot be null");
        Objects.requireNonNull(productId, "Product id cannot be null");

        productRepository.findById(productId)
                .ifPresent(cart::removeItem);
    }

    public void clearCart(Cart cart) {
        Objects.requireNonNull(cart, "Cart cannot be null");
        cart.clear();
    }

    public Map<Product, Integer> getCartItems(Cart cart) {
        Objects.requireNonNull(cart, "Cart cannot be null");
        return cart.getItems();
    }

    public boolean isEmpty(Cart cart) {
        Objects.requireNonNull(cart, "Cart cannot be null");
        return cart.isEmpty();
    }
}
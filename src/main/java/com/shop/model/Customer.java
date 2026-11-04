package com.shop.model;

import lombok.NonNull;

/**
 * Shop customer with a name, an email and an address.
 */
public record Customer(@NonNull String name, @NonNull String email, @NonNull Address address) {

    public Customer {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Email is invalid: " + email);
        }
    }
}
package com.shop.model;

import lombok.NonNull;

public record Address(@NonNull String street,
                      @NonNull String city,
                      @NonNull String postalCode,
                      @NonNull String country) {

    public Address {
        requireNotBlank(street, "Street");
        requireNotBlank(city, "City");
        requireNotBlank(postalCode, "Postal code");
        requireNotBlank(country, "Country");
    }

    private static void requireNotBlank(String value, String fieldName) {
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
    }
}
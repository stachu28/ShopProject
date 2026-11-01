package com.shop.service;

import com.shop.exceptions.ProductNotFoundException;
import com.shop.model.Cart;
import com.shop.model.Computer;
import com.shop.model.Electronics;
import com.shop.model.Product;
import com.shop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private ProductRepository productRepository;

    private CartService cartService;
    private Cart cart;
    private Product headphones;
    private Product computer;

    @BeforeEach
    void setUp() {
        cartService = new CartService(productRepository);
        cart = new Cart();
        headphones = new Electronics(1L, "Headphones", new BigDecimal("100.00"), 10);
        computer = new Computer(2L, "Laptop", new BigDecimal("5000.00"), 5, "M3", 16, 512);
    }

    @Test
    @DisplayName("addToCart adds product from repository to cart")
    void addToCartAddsProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(headphones));

        cartService.addToCart(cart, 1L, 2);

        assertEquals(1, cart.size());
        assertEquals(2, cart.totalQuantity());
        assertEquals(new BigDecimal("200.00"), cart.totalPrice());
        verify(productRepository).findById(1L);
    }

    @Test
    @DisplayName("addToCart merges quantity for the same product")
    void addToCartMergesQuantity() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(headphones));

        cartService.addToCart(cart, 1L, 2);
        cartService.addToCart(cart, 1L, 3);

        assertEquals(1, cart.size());
        assertEquals(5, cart.totalQuantity());
        assertEquals(new BigDecimal("500.00"), cart.totalPrice());
    }

    @Test
    @DisplayName("addToCart rejects missing product")
    void addToCartRejectsMissingProduct() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> cartService.addToCart(cart, 99L, 1));
        assertTrue(cart.isEmpty());
    }

    @Test
    @DisplayName("addToCart rejects null cart")
    void addToCartRejectsNullCart() {
        assertThrows(NullPointerException.class, () -> cartService.addToCart(null, 1L, 1));
        verifyNoInteractions(productRepository);
    }

    @Test
    @DisplayName("addToCart rejects null id")
    void addToCartRejectsNullId() {
        assertThrows(NullPointerException.class, () -> cartService.addToCart(cart, null, 1));
        verifyNoInteractions(productRepository);
    }

    @Test
    @DisplayName("addToCart rejects quantity <= 0")
    void addToCartRejectsNonPositiveQuantity() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(headphones));

        assertThrows(IllegalArgumentException.class, () -> cartService.addToCart(cart, 1L, 0));
        assertThrows(IllegalArgumentException.class, () -> cartService.addToCart(cart, 1L, -1));
    }

    @Test
    @DisplayName("removeFromCart removes product from cart")
    void removeFromCartRemoves() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(headphones));
        cartService.addToCart(cart, 1L, 2);

        cartService.removeFromCart(cart, 1L);

        assertTrue(cart.isEmpty());
    }

    @Test
    @DisplayName("removeFromCart removes only the given product")
    void removeFromCartRemovesOnlyGiven() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(headphones));
        when(productRepository.findById(2L)).thenReturn(Optional.of(computer));
        cartService.addToCart(cart, 1L, 2);
        cartService.addToCart(cart, 2L, 1);

        cartService.removeFromCart(cart, 1L);

        assertEquals(1, cart.size());
        assertEquals(1, cart.totalQuantity());
        assertEquals(new BigDecimal("5000.00"), cart.totalPrice());
    }

    @Test
    @DisplayName("removeFromCart does not throw for missing product")
    void removeFromCartIgnoresMissingProduct() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> cartService.removeFromCart(cart, 99L));
        assertTrue(cart.isEmpty());
    }

    @Test
    @DisplayName("removeFromCart rejects null cart")
    void removeFromCartRejectsNullCart() {
        assertThrows(NullPointerException.class, () -> cartService.removeFromCart(null, 1L));
    }

    @Test
    @DisplayName("removeFromCart rejects null id")
    void removeFromCartRejectsNullId() {
        assertThrows(NullPointerException.class, () -> cartService.removeFromCart(cart, null));
        verifyNoInteractions(productRepository);
    }

    @Test
    @DisplayName("clearCart clears the cart")
    void clearCartClears() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(headphones));
        cartService.addToCart(cart, 1L, 2);

        cartService.clearCart(cart);

        assertTrue(cart.isEmpty());
        assertEquals(0, cart.totalQuantity());
    }

    @Test
    @DisplayName("clearCart rejects null cart")
    void clearCartRejectsNull() {
        assertThrows(NullPointerException.class, () -> cartService.clearCart(null));
    }

    @Test
    @DisplayName("getCartItems returns empty map for empty cart")
    void getCartItemsEmpty() {
        assertTrue(cartService.getCartItems(cart).isEmpty());
    }

    @Test
    @DisplayName("getCartItems returns products from cart")
    void getCartItemsReturnsContent() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(headphones));
        cartService.addToCart(cart, 1L, 2);

        var items = cartService.getCartItems(cart);

        assertEquals(1, items.size());
        assertEquals(2, items.get(headphones));
    }

    @Test
    @DisplayName("getCartItems returns unmodifiable map")
    void getCartItemsIsUnmodifiable() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(headphones));
        cartService.addToCart(cart, 1L, 2);

        assertThrows(UnsupportedOperationException.class,
                () -> cartService.getCartItems(cart).put(headphones, 99));
    }

    @Test
    @DisplayName("getCartItems rejects null cart")
    void getCartItemsRejectsNull() {
        assertThrows(NullPointerException.class, () -> cartService.getCartItems(null));
    }

    @Test
    @DisplayName("isEmpty returns true for empty cart")
    void isEmptyTrueForEmpty() {
        assertTrue(cartService.isEmpty(cart));
    }

    @Test
    @DisplayName("isEmpty returns false for non-empty cart")
    void isEmptyFalseForNonEmpty() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(headphones));
        cartService.addToCart(cart, 1L, 1);

        assertFalse(cartService.isEmpty(cart));
    }

    @Test
    @DisplayName("isEmpty rejects null cart")
    void isEmptyRejectsNull() {
        assertThrows(NullPointerException.class, () -> cartService.isEmpty(null));
    }
}
package com.shop.service;

import com.shop.exceptions.ProductNotFoundException;
import com.shop.model.Computer;
import com.shop.model.Electronics;
import com.shop.model.Product;
import com.shop.model.ProductType;
import com.shop.model.Smartphone;
import com.shop.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductManagerTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductManager productManager;

    private static Electronics electronics(Long id) {
        return new Electronics(id, "Product " + id, new BigDecimal("10"), 5);
    }

    private static Computer computer(Long id) {
        return new Computer(id, "Computer " + id, new BigDecimal("5000"), 1, "M3", 16, 512);
    }

    @Test
    @DisplayName("addProduct saves product in repository")
    void addProductSaves() {
        Product product = electronics(1L);
        when(productRepository.existsById(1L)).thenReturn(false);

        productManager.addProduct(product);

        verify(productRepository).save(product);
    }

    @Test
    @DisplayName("addProduct throws Null Pointer Exception in null product")
    void addProductRejectsNull() {
        assertThrows(NullPointerException.class, () -> productManager.addProduct(null));
        verifyNoInteractions(productRepository);
    }

    @Test
    @DisplayName("addProduct rejects duplicate id")
    void addProductRejectsDuplicateId() {
        when(productRepository.existsById(1L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> productManager.addProduct(electronics(1L)));
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("removeProduct removes a product")
    void removeProductRemoves() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productManager.removeProduct(1L);

        verify(productRepository).deleteById(1L);
    }

    @Test
    @DisplayName("removeProduct rejects missing product")
    void removeProductRejectsMissing() {
        when(productRepository.existsById(99L)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productManager.removeProduct(99L));
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("removeProduct throws Null Pointer Exception in null product")
    void removeProductRejectsNull() {
        assertThrows(NullPointerException.class, () -> productManager.removeProduct(null));
        verifyNoInteractions(productRepository);
    }

    @Test
    @DisplayName("update saves and updated product")
    void updateSavesProduct() {
        Product existing = electronics(1L);
        Product updated = new Electronics(1L, "New name", new BigDecimal("20"), 3);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));

        productManager.update(updated);

        verify(productRepository).save(updated);
    }

    @Test
    @DisplayName("update rejects missing product")
    void updateRejectsMissing() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productManager.update(electronics(99L)));
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("update rejects product type change")
    void updateRejectsTypeChange() {
        Product existing = electronics(1L);
        Product smartphone = new Smartphone(1L, "Phone", new BigDecimal("3999"), 1, "Black", 4000);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));

        assertThrows(IllegalArgumentException.class, () -> productManager.update(smartphone));
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("update throws Null Pointer Exception in null update")
    void updateRejectsNull() {
        assertThrows(NullPointerException.class, () -> productManager.update(null));
        verifyNoInteractions(productRepository);
    }

    @Test
    @DisplayName("findById returns product from repository")
    void findByIdReturnsProduct() {
        Product product = electronics(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertEquals(Optional.of(product), productManager.findById(1L));
    }

    @Test
    @DisplayName("findAll returns all products within repository")
    void findAllReturnsAllProducts() {
        List<Product> products = List.of(electronics(1L), computer(2L));
        when(productRepository.findAll()).thenReturn(products);

        assertEquals(2, productManager.findAll().size());
        assertEquals(products, productManager.findAll());
    }

    @Test
    @DisplayName("findByType returns all products with given type within repository")
    void findByTypeReturnsFilteredProducts() {
        List<Product> computers = List.of(computer(2L));
        when(productRepository.findByType(ProductType.COMPUTER)).thenReturn(computers);

        assertEquals(1, productManager.findByType(ProductType.COMPUTER).size());
        assertEquals(computers, productManager.findByType(ProductType.COMPUTER));
    }

    @Test
    @DisplayName("findByType throws Null Pointer Exception for null type")
    void findByTypeRejectsNull() {
        assertThrows(NullPointerException.class, () -> productManager.findByType(null));
        verifyNoInteractions(productRepository);
    }
}
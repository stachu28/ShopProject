package com.shop.repository;

import com.shop.model.Address;
import com.shop.model.Customer;
import com.shop.model.Electronics;
import com.shop.model.Order;
import com.shop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryOrderRepositoryTest {

    private static final Address ADDRESS = new Address("Prosta 1", "Gliwice", "02-300", "Poland");
    private static final Customer ANNA = new Customer("Anna Nowak", "anna@example.com", ADDRESS);
    private static final Customer JAN = new Customer("Jan Kowalski", "jan@example.com", ADDRESS);
    private static final LocalDateTime DATE = LocalDateTime.of(2026, 10, 23, 12, 0);

    private InMemoryOrderRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryOrderRepository();
    }

    private static Order order(Customer customer) {
        Product p = new Electronics(1L, "X", new BigDecimal("10.00"), 10);
        return new Order(customer, Map.of(p, 1), DATE);
    }

    @Test
    @DisplayName("findAll returns empty list when nothing saved")
    void findAllEmptyByDefault() {
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    @DisplayName("save stores the order")
    void saveStoresOrder() {
        Order order = order(ANNA);
        repository.save(order);
        assertEquals(List.of(order), repository.findAll());
    }

    @Test
    @DisplayName("save throws Null Pointer Exception for null order")
    void saveRejectsNull() {
        assertThrows(NullPointerException.class, () -> repository.save(null));
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    @DisplayName("findAll returns unmodifiable list")
    void findAllReturnsUnmodifiableList() {
        repository.save(order(ANNA));
        assertThrows(UnsupportedOperationException.class,
                () -> repository.findAll().clear());
    }

    @Test
    @DisplayName("findAll returns snapshot of current state")
    void findAllReturnsSnapshot() {
        repository.save(order(ANNA));
        List<Order> snapshot = repository.findAll();
        repository.save(order(JAN));
        assertEquals(1, snapshot.size());
        assertEquals(2, repository.findAll().size());
    }

    @Test
    @DisplayName("findByCustomerEmail returns only matching orders")
    void findByCustomerEmailFilters() {
        Order annaOrder = order(ANNA);
        Order janOrder = order(JAN);
        repository.save(annaOrder);
        repository.save(janOrder);
        List<Order> result = repository.findByCustomerEmail("anna@example.com");
        assertEquals(List.of(annaOrder), result);
    }

    @Test
    @DisplayName("findByCustomerEmail ignores case")
    void findByCustomerEmailIgnoresCase() {
        repository.save(order(ANNA));
        assertEquals(1, repository.findByCustomerEmail("ANNA@EXAMPLE.COM").size());
    }

    @Test
    @DisplayName("findByCustomerEmail returns empty list when no match")
    void findByCustomerEmailEmptyWhenNoMatch() {
        repository.save(order(ANNA));
        assertTrue(repository.findByCustomerEmail("nobody@example.com").isEmpty());
    }

    @Test
    @DisplayName("findByCustomerEmail rejects null email")
    void findByCustomerEmailRejectsNull() {
        assertThrows(NullPointerException.class,
                () -> repository.findByCustomerEmail(null));
    }
}
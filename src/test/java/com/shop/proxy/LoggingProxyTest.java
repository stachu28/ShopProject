package com.shop.proxy;

import com.shop.model.Electronics;
import com.shop.model.Product;
import com.shop.repository.InMemoryProductRepository;
import com.shop.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class LoggingProxyTest {

    private final Logger logger = Logger.getLogger(LoggingProxy.class.getName());
    private final List<String> messages = new ArrayList<>();
    private final Handler handler = new Handler() {
        @Override
        public void publish(LogRecord record) {
            messages.add(record.getMessage());
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() {
        }
    };

    private Level originalLevel;
    private ProductRepository repository;
    private Product product;

    @BeforeEach
    void setUp() {
        repository = LoggingProxy.withLogging(new InMemoryProductRepository(), ProductRepository.class);
        product = new Electronics(1L, "X", BigDecimal.TEN, 1);
        originalLevel = logger.getLevel();
        logger.setLevel(Level.ALL);
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);
    }

    @AfterEach
    void tearDown() {
        logger.removeHandler(handler);
        logger.setLevel(originalLevel);
    }

    @Test
    @DisplayName("Proxy delegates calls and returns the target's result")
    void delegatesToTarget() {
        repository.save(product);

        assertEquals(Optional.of(product), repository.findById(1L));
    }

    @Test
    @DisplayName("Every call is logged with the method name")
    void logsEveryCall() {
        repository.save(product);
        repository.findAll();

        assertEquals(2, messages.size());
        assertTrue(messages.get(0).startsWith("save took "));
        assertTrue(messages.get(1).startsWith("findAll took "));
    }

    @Test
    @DisplayName("Exception from the target reaches the caller unchanged and is still logged")
    void rethrowsOriginalException() {
        ProductRepository failing = LoggingProxy.withLogging(new InMemoryProductRepository() {
            @Override
            public boolean existsById(Long id) {
                throw new IllegalStateException("db down");
            }
        }, ProductRepository.class);

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> failing.existsById(1L));

        assertEquals("db down", e.getMessage());
        assertEquals(1, messages.size());
    }
}
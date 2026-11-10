package com.shop.config;

import com.shop.model.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class SettingsTest {

    @Test
    @DisplayName("INSTANCE is always the same object")
    void instanceIsSingleton() {
        assertSame(Settings.INSTANCE, Settings.INSTANCE);
    }

    @Test
    @DisplayName("Only one enum constant exists")
    void onlyOneConstant() {
        assertEquals(1, Settings.values().length);
    }

    @Test
    @DisplayName("Enum cannot be instantiated via reflection")
    void reflectionCannotCreateNewInstance() {
        assertThrows(Exception.class, () -> {
            var constructor = Settings.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }

    @Test
    @DisplayName("Shop name is set")
    void shopName() {
        assertEquals("Sklep", Settings.INSTANCE.getShopName());
    }

    @Test
    @DisplayName("Thresholds are set and ordered")
    void thresholds() {
        assertEquals(new BigDecimal("5000.00"), Settings.INSTANCE.getOrderLimit());
        assertEquals(new BigDecimal("10000.00"), Settings.INSTANCE.getBigOrderLimit());
        assertEquals(new BigDecimal("2000.00"), Settings.INSTANCE.getManualApprovalThreshold());
        assertEquals(new BigDecimal("200.00"), Settings.INSTANCE.getFreeDeliveryThreshold());

        assertTrue(Settings.INSTANCE.getOrderLimit()
                .compareTo(Settings.INSTANCE.getBigOrderLimit()) < 0);
        assertTrue(Settings.INSTANCE.getManualApprovalThreshold()
                .compareTo(Settings.INSTANCE.getOrderLimit()) < 0);
    }

    @Test
    @DisplayName("VAT rate is 23%")
    void vatRate() {
        assertEquals(new BigDecimal("0.23"), Settings.INSTANCE.getVatRate());
    }

    @Test
    @DisplayName("SHOP_ZONE is Europe/Warsaw")
    void shopZone() {
        assertEquals(ZoneId.of("Europe/Warsaw"), Settings.SHOP_ZONE);
    }

    @Test
    @DisplayName("Order.SHOP_ZONE delegates to Settings.SHOP_ZONE")
    void orderZoneDelegatesToSettings() {
        assertEquals(Settings.SHOP_ZONE, Order.SHOP_ZONE);
    }
}
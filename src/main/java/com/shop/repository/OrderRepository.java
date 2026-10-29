package com.shop.repository;

import com.shop.model.Order;

import java.util.List;

public interface OrderRepository {

    void save(Order order);

    List<Order> findAll();

    List<Order> findByCustomerEmail(String email);
}
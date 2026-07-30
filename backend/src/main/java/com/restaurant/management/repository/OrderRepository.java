package com.restaurant.management.repository;


import com.restaurant.management.entity.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import com.restaurant.management.entity.OrderStatus;

import com.restaurant.management.entity.Order;

import java.util.List;



public interface OrderRepository
        extends JpaRepository<Order,Long> {


    List<Order> findByCustomerId(Long customerId);
    List<Order> findByStatus(OrderStatus status);

}

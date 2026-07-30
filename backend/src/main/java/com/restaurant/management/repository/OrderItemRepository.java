package com.restaurant.management.repository;


import com.restaurant.management.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface OrderItemRepository
        extends JpaRepository<OrderItem,Long> {

}

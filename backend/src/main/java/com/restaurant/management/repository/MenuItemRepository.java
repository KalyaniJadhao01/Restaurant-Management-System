package com.restaurant.management.repository;


import com.restaurant.management.entity.MenuItem;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;



public interface MenuItemRepository
        extends JpaRepository<MenuItem, Long> {


    List<MenuItem> findByCategoryId(Long categoryId);


    List<MenuItem> findByAvailableTrue();
    List<MenuItem> findByNameContainingIgnoreCase(String keyword);

    List<MenuItem> findByAvailable(boolean available);
}
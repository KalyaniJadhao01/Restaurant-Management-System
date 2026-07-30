package com.restaurant.management.repository;


import com.restaurant.management.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface CategoryRepository
        extends JpaRepository<Category, Long> {


    Optional<Category> findByNameIgnoreCase(String name);
    List<Category> findByNameContainingIgnoreCase(String keyword);

    boolean existsByNameIgnoreCase(String name);

}
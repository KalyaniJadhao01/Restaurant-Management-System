package com.restaurant.management.repository;


import com.restaurant.management.entity.Customer;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;
import java.util.List;


public interface CustomerRepository
        extends JpaRepository<Customer,Long> {


    Optional<Customer> findByPhone(String phone);


    Optional<Customer> findByEmail(String email);


    List<Customer> findByNameContainingIgnoreCase(String name);

    List<Customer> findByPhoneContaining(String phone);

    List<Customer> findByEmailContainingIgnoreCase(String email);


    boolean existsByPhone(String phone);


    boolean existsByEmail(String email);

}

package com.restaurant.management.repository;


import com.restaurant.management.entity.Bill;


import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;



public interface BillRepository
        extends JpaRepository<Bill,Long> {



    Optional<Bill> findByOrderId(Long orderId);


}
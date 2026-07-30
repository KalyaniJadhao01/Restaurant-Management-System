package com.restaurant.management.repository;


import com.restaurant.management.entity.Payment;


import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;



public interface PaymentRepository
        extends JpaRepository<Payment,Long> {



    Optional<Payment> findByBillId(Long billId);


}
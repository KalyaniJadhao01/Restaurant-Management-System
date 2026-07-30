package com.restaurant.management.service;


import com.restaurant.management.dto.PaymentRequest;
import com.restaurant.management.dto.PaymentResponse;
import com.restaurant.management.entity.PaymentStatus;
import org.springframework.data.domain.Page;


import java.util.List;


public interface PaymentService {


    PaymentResponse createPayment(
            PaymentRequest request
    );


    PaymentResponse getPaymentById(
            Long id
    );


    PaymentResponse getPaymentByBillId(
            Long billId
    );


    Page<PaymentResponse> getAllPayments(
            int page,
            int size,
            String sortBy,
            String direction
    );


    PaymentResponse updatePaymentStatus(
            Long id,
            PaymentStatus status
    );

}
package com.restaurant.management.controller;


import com.restaurant.management.dto.PaymentRequest;
import com.restaurant.management.dto.PaymentResponse;


import com.restaurant.management.entity.PaymentStatus;


import com.restaurant.management.service.PaymentService;
import org.springframework.data.domain.Page;

import jakarta.validation.Valid;


import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;


import java.util.List;



@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {



    private final PaymentService paymentService;




    @PreAuthorize("hasAnyRole('ADMIN','CASHIER')")
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(

            @Valid @RequestBody PaymentRequest request

    ){


        return new ResponseEntity<>(

                paymentService.createPayment(request),

                HttpStatus.CREATED

        );

    }





    @PreAuthorize("hasAnyRole('ADMIN','CASHIER')")
    @GetMapping
    public ResponseEntity<Page<PaymentResponse>> getAllPayments(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "id") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(

                paymentService.getAllPayments(
                        page,
                        size,
                        sortBy,
                        direction
                )

        );

    }





    @PreAuthorize("hasAnyRole('ADMIN','CASHIER')")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(

            @PathVariable Long id

    ){


        return ResponseEntity.ok(

                paymentService.getPaymentById(id)

        );

    }






    @PreAuthorize("hasAnyRole('ADMIN','CASHIER')")
    @GetMapping("/bill/{billId}")
    public ResponseEntity<PaymentResponse> getByBill(

            @PathVariable Long billId

    ){


        return ResponseEntity.ok(

                paymentService.getPaymentByBillId(
                        billId
                )

        );

    }






    @PreAuthorize("hasAnyRole('ADMIN','CASHIER')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<PaymentResponse> updateStatus(

            @PathVariable Long id,

            @RequestParam PaymentStatus status

    ){


        return ResponseEntity.ok(

                paymentService.updatePaymentStatus(
                        id,
                        status
                )

        );

    }


}
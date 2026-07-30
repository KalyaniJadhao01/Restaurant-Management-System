package com.restaurant.management.service.impl;


import com.restaurant.management.dto.PaymentRequest;
import com.restaurant.management.dto.PaymentResponse;


import com.restaurant.management.entity.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.restaurant.management.exception.ResourceAlreadyExistsException;
import com.restaurant.management.exception.ResourceNotFoundException;


import com.restaurant.management.repository.BillRepository;
import com.restaurant.management.repository.PaymentRepository;


import com.restaurant.management.service.PaymentService;


import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;



@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {



    private final PaymentRepository paymentRepository;


    private final BillRepository billRepository;





    @Override
    public PaymentResponse createPayment(
            PaymentRequest request
    ){



        if(paymentRepository
                .findByBillId(request.getBillId())
                .isPresent()){


            throw new ResourceAlreadyExistsException(
                    "Payment already exists for bill: "
                            + request.getBillId()
            );

        }





        Bill bill =
                billRepository.findById(
                                request.getBillId()
                        )

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Bill not found with id: "
                                                + request.getBillId()
                                ));






        Payment payment =
                Payment.builder()

                        .bill(bill)

                        .amount(
                                request.getAmount()
                        )

                        .paymentMethod(
                                request.getPaymentMethod()
                        )

                        .status(
                                PaymentStatus.SUCCESS
                        )

                        .transactionReference(
                                generateTransactionReference()
                        )

                        .build();






        Payment savedPayment =
                paymentRepository.save(payment);




        // Mark bill as paid
        bill.setStatus(BillStatus.PAID);

        billRepository.save(bill);




        return mapToResponse(savedPayment);

    }








    @Override
    public PaymentResponse getPaymentById(
            Long id
    ){


        Payment payment =
                paymentRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment not found with id: "
                                                + id
                                ));




        return mapToResponse(payment);

    }








    @Override
    public PaymentResponse getPaymentByBillId(
            Long billId
    ){


        Payment payment =
                paymentRepository.findByBillId(billId)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment not found for bill: "
                                                + billId
                                ));




        return mapToResponse(payment);

    }








    @Override
    public Page<PaymentResponse> getAllPayments(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Payment> paymentPage = paymentRepository.findAll(pageable);

        return paymentPage.map(this::mapToResponse);
    }








    @Override
    public PaymentResponse updatePaymentStatus(
            Long id,
            PaymentStatus status
    ){


        Payment payment =
                paymentRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment not found with id: "
                                                + id
                                ));




        payment.setStatus(status);



        return mapToResponse(
                paymentRepository.save(payment)
        );

    }








    private String generateTransactionReference(){

        return "TXN-"
                + UUID.randomUUID()
                .toString()
                .substring(0,8)
                .toUpperCase();

    }








    private PaymentResponse mapToResponse(
            Payment payment
    ){


        return PaymentResponse.builder()

                .id(
                        payment.getId()
                )

                .billId(
                        payment.getBill().getId()
                )

                .amount(
                        payment.getAmount()
                )

                .paymentMethod(
                        payment.getPaymentMethod()
                )

                .status(
                        payment.getStatus()
                )

                .transactionReference(
                        payment.getTransactionReference()
                )

                .createdAt(
                        payment.getCreatedAt()
                )

                .build();

    }


}
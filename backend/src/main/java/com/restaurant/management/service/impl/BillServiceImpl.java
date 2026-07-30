package com.restaurant.management.service.impl;


import com.restaurant.management.dto.BillResponse;

import com.restaurant.management.entity.*;

import com.restaurant.management.exception.ResourceAlreadyExistsException;
import com.restaurant.management.exception.ResourceNotFoundException;

import com.restaurant.management.repository.BillRepository;
import com.restaurant.management.repository.OrderRepository;

import com.restaurant.management.service.BillService;


import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.List;



@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {



    private final BillRepository billRepository;

    private final OrderRepository orderRepository;



    private static final BigDecimal TAX_RATE =
            BigDecimal.valueOf(0.05);





    @Override
    public BillResponse generateBill(
            Long orderId,
            BigDecimal discountAmount
    ){



        if(billRepository.findByOrderId(orderId)
                .isPresent()){


            throw new ResourceAlreadyExistsException(
                    "Bill already generated for order: "
                            + orderId
            );

        }





        Order order =
                orderRepository.findById(orderId)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found with id: "
                                                + orderId
                                ));





        BigDecimal subtotal =
                order.getTotalAmount();





        BigDecimal taxAmount =
                subtotal.multiply(TAX_RATE)
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );





        if(discountAmount == null){

            discountAmount =
                    BigDecimal.ZERO;

        }





        BigDecimal totalAmount =
                subtotal
                        .add(taxAmount)
                        .subtract(discountAmount);





        if(totalAmount.compareTo(BigDecimal.ZERO)<0){

            totalAmount = BigDecimal.ZERO;

        }





        Bill bill = Bill.builder()

                .order(order)

                .subtotal(subtotal)

                .taxAmount(taxAmount)

                .discountAmount(discountAmount)

                .totalAmount(totalAmount)

                .status(BillStatus.UNPAID)

                .build();





        return mapToResponse(
                billRepository.save(bill)
        );

    }






    @Override
    public BillResponse getBillById(Long id){


        Bill bill =
                billRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Bill not found with id: "
                                                + id
                                ));



        return mapToResponse(bill);

    }






    @Override
    public BillResponse getBillByOrderId(Long orderId){


        Bill bill =
                billRepository.findByOrderId(orderId)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Bill not found for order: "
                                                + orderId
                                ));



        return mapToResponse(bill);

    }






    @Override
    public List<BillResponse> getAllBills(){


        return billRepository.findAll()

                .stream()

                .map(this::mapToResponse)

                .toList();

    }






    @Override
    public BillResponse updateBillStatus(
            Long id,
            BillStatus status
    ){


        Bill bill =
                billRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Bill not found with id: "
                                                + id
                                ));




        bill.setStatus(status);



        return mapToResponse(
                billRepository.save(bill)
        );

    }







    private BillResponse mapToResponse(
            Bill bill
    ){


        return BillResponse.builder()

                .id(bill.getId())

                .orderId(
                        bill.getOrder().getId()
                )

                .subtotal(
                        bill.getSubtotal()
                )

                .taxAmount(
                        bill.getTaxAmount()
                )

                .discountAmount(
                        bill.getDiscountAmount()
                )

                .totalAmount(
                        bill.getTotalAmount()
                )

                .status(
                        bill.getStatus()
                )

                .createdAt(
                        bill.getCreatedAt()
                )

                .build();

    }

}
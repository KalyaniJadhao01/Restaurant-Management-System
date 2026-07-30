package com.restaurant.management.dto;


import com.restaurant.management.entity.PaymentMethod;
import com.restaurant.management.entity.PaymentStatus;


import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;



@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {



    private Long id;



    private Long billId;



    private BigDecimal amount;



    private PaymentMethod paymentMethod;



    private PaymentStatus status;



    private String transactionReference;



    private LocalDateTime createdAt;

}
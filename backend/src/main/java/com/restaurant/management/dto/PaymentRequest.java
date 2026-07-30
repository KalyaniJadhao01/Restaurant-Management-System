package com.restaurant.management.dto;


import com.restaurant.management.entity.PaymentMethod;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;


import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;



@Getter
@Setter
public class PaymentRequest {



    @NotNull(message = "Bill id is required")
    private Long billId;



    @NotNull(message = "Payment amount required")
    @DecimalMin(
            value = "0.0",
            message = "Amount cannot be negative"
    )
    private BigDecimal amount;



    @NotNull(message = "Payment method required")
    private PaymentMethod paymentMethod;


}
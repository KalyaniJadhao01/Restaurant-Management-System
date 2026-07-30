package com.restaurant.management.dto;


import jakarta.validation.constraints.DecimalMin;

import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;



@Getter
@Setter
public class BillRequest {



    @DecimalMin(
            value = "0.0",
            message = "Discount cannot be negative"
    )
    private BigDecimal discountAmount;


}

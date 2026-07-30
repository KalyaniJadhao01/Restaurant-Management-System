package com.restaurant.management.dto;


import com.restaurant.management.entity.BillStatus;


import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;



@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillResponse {



    private Long id;



    private Long orderId;



    private BigDecimal subtotal;



    private BigDecimal taxAmount;



    private BigDecimal discountAmount;



    private BigDecimal totalAmount;



    private BillStatus status;



    private LocalDateTime createdAt;


}
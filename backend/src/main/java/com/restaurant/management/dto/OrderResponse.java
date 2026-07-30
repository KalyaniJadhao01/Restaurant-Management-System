package com.restaurant.management.dto;


import com.restaurant.management.entity.OrderStatus;

import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;



@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {



    private Long id;



    private Long customerId;



    private String customerName;



    private Long tableId;



    private String tableNumber;



    private BigDecimal totalAmount;



    private OrderStatus status;



    private List<OrderItemResponse> items;



    private LocalDateTime createdAt;


}
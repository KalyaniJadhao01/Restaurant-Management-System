package com.restaurant.management.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;


import java.util.List;



@Getter
@Setter
public class OrderRequest {



    @NotNull(message = "Customer id required")
    private Long customerId;



    @NotNull(message = "Table id required")
    private Long tableId;



    @NotEmpty(message = "Order items required")
    private List<OrderItemRequest> items;


}

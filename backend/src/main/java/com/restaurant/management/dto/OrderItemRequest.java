package com.restaurant.management.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class OrderItemRequest {



    @NotNull(message = "Menu item id required")
    private Long menuItemId;



    @NotNull(message = "Quantity required")
    @Min(
            value = 1,
            message = "Quantity must be minimum 1"
    )
    private Integer quantity;

}
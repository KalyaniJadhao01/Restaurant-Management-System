package com.restaurant.management.dto;


import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class TableRequest {



    @NotBlank(message = "Table number required")
    private String tableNumber;



    @NotNull(message = "Capacity required")
    @Min(value = 1,
            message = "Capacity must be at least 1")
    private Integer capacity;



}
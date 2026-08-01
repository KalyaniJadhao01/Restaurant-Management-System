package com.restaurant.management.dto;

import com.restaurant.management.entity.TableStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableRequest {

    @NotBlank(message = "Table number required")
    private String tableNumber;

    @NotNull(message = "Capacity required")
    @Min(
            value = 1,
            message = "Capacity must be at least 1"
    )
    private Integer capacity;

    @NotNull(message = "Status is required")
    private TableStatus status;

}
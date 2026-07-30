package com.restaurant.management.dto;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;



@Getter
@Setter
public class MenuItemRequest {


    @NotBlank(message = "Menu item name is required")
    @Size(min = 2,max = 100)
    private String name;



    @Size(max = 500)
    private String description;



    @NotNull(message = "Price is required")
    @DecimalMin(
            value = "1.0",
            message = "Price must be greater than zero"
    )
    private BigDecimal price;



    private String imageUrl;



    private Boolean available;



    @NotNull(message = "Category id is required")
    private Long categoryId;


}

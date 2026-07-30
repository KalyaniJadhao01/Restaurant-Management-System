package com.restaurant.management.dto;


import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class CustomerRequest {


    @NotBlank(message = "Customer name is required")
    @Size(min = 2,max = 100)
    private String name;



    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must contain 10 digits"
    )
    private String phone;



    @Email(message = "Invalid email format")
    private String email;



    private String address;


}

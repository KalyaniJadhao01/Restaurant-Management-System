package com.restaurant.management.dto;


import lombok.*;


import java.time.LocalDateTime;



@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {


    private Long id;


    private String name;


    private String phone;


    private String email;


    private String address;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;

}
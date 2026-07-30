package com.restaurant.management.dto;


import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;



@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemResponse {


    private Long id;


    private String name;


    private String description;


    private BigDecimal price;


    private String imageUrl;


    private Boolean available;


    private Long categoryId;


    private String categoryName;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;

}
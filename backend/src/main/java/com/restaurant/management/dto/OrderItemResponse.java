package com.restaurant.management.dto;


import lombok.*;


import java.math.BigDecimal;



@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {


    private Long id;


    private Long menuItemId;


    private String menuItemName;


    private Integer quantity;


    private BigDecimal price;


    private BigDecimal subtotal;

}

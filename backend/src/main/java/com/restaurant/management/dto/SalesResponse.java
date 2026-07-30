package com.restaurant.management.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesResponse {

    private String period;

    private BigDecimal totalSales;

}
package com.restaurant.management.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {

    private Long totalCustomers;

    private Long totalOrders;

    private Long totalBills;

    private Long totalPayments;

    private BigDecimal totalRevenue;

    private Long pendingOrders;

    private Long completedOrders;

}
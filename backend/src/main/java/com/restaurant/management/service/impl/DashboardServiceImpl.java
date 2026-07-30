package com.restaurant.management.service.impl;

import com.restaurant.management.dto.DashboardSummaryResponse;
import com.restaurant.management.dto.PopularMenuItemResponse;
import com.restaurant.management.dto.SalesResponse;

import com.restaurant.management.entity.OrderStatus;

import com.restaurant.management.projection.PopularMenuItemProjection;

import com.restaurant.management.repository.BillRepository;
import com.restaurant.management.repository.CustomerRepository;
import com.restaurant.management.repository.DashboardRepository;
import com.restaurant.management.repository.PaymentRepository;

import com.restaurant.management.service.DashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final CustomerRepository customerRepository;

    private final BillRepository billRepository;

    private final PaymentRepository paymentRepository;

    private final DashboardRepository dashboardRepository;

    @Override
    public DashboardSummaryResponse getDashboardSummary() {

        return DashboardSummaryResponse.builder()

                .totalCustomers(
                        customerRepository.count()
                )

                .totalOrders(
                        dashboardRepository.getTotalOrders()
                )

                .totalBills(
                        billRepository.count()
                )

                .totalPayments(
                        paymentRepository.count()
                )

                .totalRevenue(
                        dashboardRepository.getTotalRevenue()
                )

                .pendingOrders(
                        dashboardRepository.getOrdersByStatus(
                                OrderStatus.PENDING
                        )
                )

                .completedOrders(
                        dashboardRepository.getOrdersByStatus(
                                OrderStatus.COMPLETED
                        )
                )

                .build();

    }

    @Override
    public SalesResponse getTotalSales() {

        BigDecimal revenue =
                dashboardRepository.getTotalRevenue();

        return SalesResponse.builder()

                .period("Overall")

                .totalSales(revenue)

                .build();

    }

    @Override
    public List<PopularMenuItemResponse> getPopularItems() {

        return dashboardRepository
                .getPopularItems()

                .stream()

                .map(this::mapToResponse)

                .toList();

    }

    private PopularMenuItemResponse mapToResponse(
            PopularMenuItemProjection projection
    ) {

        return PopularMenuItemResponse.builder()

                .menuItemName(
                        projection.getMenuItemName()
                )

                .quantitySold(
                        projection.getQuantitySold()
                )

                .build();

    }

}
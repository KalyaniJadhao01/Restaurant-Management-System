package com.restaurant.management.controller;

import com.restaurant.management.dto.DashboardSummaryResponse;
import com.restaurant.management.dto.PopularMenuItemResponse;
import com.restaurant.management.dto.SalesResponse;

import com.restaurant.management.service.DashboardService;
import org.springframework.security.access.prepost.PreAuthorize;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryResponse> getSummary() {

        return ResponseEntity.ok(
                dashboardService.getDashboardSummary()
        );

    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/sales")
    public ResponseEntity<SalesResponse> getSales() {

        return ResponseEntity.ok(
                dashboardService.getTotalSales()
        );

    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/popular-items")
    public ResponseEntity<List<PopularMenuItemResponse>> getPopularItems() {

        return ResponseEntity.ok(
                dashboardService.getPopularItems()
        );

    }

}
package com.restaurant.management.service;

import com.restaurant.management.dto.DashboardSummaryResponse;
import com.restaurant.management.dto.PopularMenuItemResponse;
import com.restaurant.management.dto.SalesResponse;
import org.springframework.data.domain.Page;


import java.util.List;

public interface DashboardService {

    DashboardSummaryResponse getDashboardSummary();

    SalesResponse getTotalSales();

    List<PopularMenuItemResponse> getPopularItems();

}
package com.restaurant.management.repository;

import com.restaurant.management.entity.Order;
import com.restaurant.management.entity.OrderStatus;
import com.restaurant.management.projection.PopularMenuItemProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface DashboardRepository extends JpaRepository<Order, Long> {

    @Query("""
            SELECT COUNT(o)
            FROM Order o
            """)
    Long getTotalOrders();

    @Query("""
            SELECT COUNT(o)
            FROM Order o
            WHERE o.status = :status
            """)
    Long getOrdersByStatus(OrderStatus status);

    @Query("""
            SELECT COALESCE(SUM(p.amount),0)
            FROM Payment p
            WHERE p.status = com.restaurant.management.entity.PaymentStatus.SUCCESS
            """)
    BigDecimal getTotalRevenue();

    @Query("""
            SELECT
                oi.menuItem.name AS menuItemName,
                SUM(oi.quantity) AS quantitySold
            FROM OrderItem oi
            GROUP BY oi.menuItem.name
            ORDER BY SUM(oi.quantity) DESC
            """)
    List<PopularMenuItemProjection> getPopularItems();

}
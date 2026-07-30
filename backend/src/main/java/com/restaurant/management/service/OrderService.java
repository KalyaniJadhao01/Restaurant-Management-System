package com.restaurant.management.service;


import com.restaurant.management.dto.OrderRequest;
import com.restaurant.management.dto.OrderResponse;
import com.restaurant.management.entity.OrderStatus;
import org.springframework.data.domain.Page;

import java.util.List;


public interface OrderService {


    OrderResponse createOrder(OrderRequest request);


    OrderResponse getOrderById(Long id);


    Page<OrderResponse> getAllOrders(
            int page,
            int size,
            String sortBy,
            String direction
    );


    List<OrderResponse> getCustomerOrders(Long customerId);


    OrderResponse updateOrderStatus(Long id,
                                    OrderStatus status);


    void deleteOrder(Long id);

}
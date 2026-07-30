package com.restaurant.management.service.impl;


import com.restaurant.management.dto.*;

import com.restaurant.management.entity.*;

import com.restaurant.management.exception.ResourceNotFoundException;

import com.restaurant.management.repository.*;

import com.restaurant.management.service.OrderService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Service;


import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;



@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {



    private final OrderRepository orderRepository;

    private final CustomerRepository customerRepository;

    private final TableRepository tableRepository;

    private final MenuItemRepository menuItemRepository;





    @Override
    public OrderResponse createOrder(
            OrderRequest request
    ) {



        Customer customer =
                customerRepository.findById(
                                request.getCustomerId()
                        )

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found with id: "
                                                + request.getCustomerId()
                                ));




        RestaurantTable table =
                tableRepository.findById(
                                request.getTableId()
                        )

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Table not found with id: "
                                                + request.getTableId()
                                ));





        Order order = Order.builder()

                .customer(customer)

                .table(table)

                .status(OrderStatus.PENDING)

                .totalAmount(BigDecimal.ZERO)

                .items(new ArrayList<>())

                .build();




        BigDecimal totalAmount =
                BigDecimal.ZERO;





        for(OrderItemRequest itemRequest :
                request.getItems()) {



            MenuItem menuItem =
                    menuItemRepository.findById(
                                    itemRequest.getMenuItemId()
                            )

                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Menu item not found with id: "
                                                    + itemRequest.getMenuItemId()
                                    ));




            BigDecimal price =
                    menuItem.getPrice();



            BigDecimal subtotal =
                    price.multiply(
                            BigDecimal.valueOf(
                                    itemRequest.getQuantity()
                            )
                    );



            OrderItem orderItem =
                    OrderItem.builder()

                            .order(order)

                            .menuItem(menuItem)

                            .quantity(
                                    itemRequest.getQuantity()
                            )

                            .price(price)

                            .subtotal(subtotal)

                            .build();




            order.getItems()
                    .add(orderItem);



            totalAmount =
                    totalAmount.add(subtotal);


        }





        order.setTotalAmount(totalAmount);



        Order savedOrder =
                orderRepository.save(order);



        return mapToResponse(savedOrder);

    }






    @Override
    public OrderResponse getOrderById(Long id) {


        Order order =
                orderRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found with id: "
                                                + id
                                ));



        return mapToResponse(order);

    }






    @Override
    public Page<OrderResponse> getAllOrders(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Order> orderPage = orderRepository.findAll(pageable);

        return orderPage.map(this::mapToResponse);
    }






    @Override
    public List<OrderResponse> getCustomerOrders(
            Long customerId
    ) {


        return orderRepository
                .findByCustomerId(customerId)

                .stream()

                .map(this::mapToResponse)

                .toList();

    }







    @Override
    public OrderResponse updateOrderStatus(
            Long id,
            OrderStatus status
    ) {


        Order order =
                orderRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found with id: "
                                                + id
                                ));



        order.setStatus(status);



        return mapToResponse(
                orderRepository.save(order)
        );

    }






    @Override
    public void deleteOrder(Long id) {


        Order order =
                orderRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found with id: "
                                                + id
                                ));



        orderRepository.delete(order);

    }







    private OrderResponse mapToResponse(
            Order order
    ){


        List<OrderItemResponse> items =
                order.getItems()
                        .stream()

                        .map(item ->

                                OrderItemResponse.builder()

                                        .id(item.getId())

                                        .menuItemId(
                                                item.getMenuItem().getId()
                                        )

                                        .menuItemName(
                                                item.getMenuItem().getName()
                                        )

                                        .quantity(
                                                item.getQuantity()
                                        )

                                        .price(
                                                item.getPrice()
                                        )

                                        .subtotal(
                                                item.getSubtotal()
                                        )

                                        .build()

                        )

                        .toList();




        return OrderResponse.builder()

                .id(order.getId())

                .customerId(
                        order.getCustomer().getId()
                )

                .customerName(
                        order.getCustomer().getName()
                )

                .tableId(
                        order.getTable().getId()
                )

                .tableNumber(
                        order.getTable().getTableNumber()
                )

                .totalAmount(
                        order.getTotalAmount()
                )

                .status(
                        order.getStatus()
                )

                .items(items)

                .createdAt(
                        order.getCreatedAt()
                )

                .build();

    }


}

package com.restaurant.management.controller;


import com.restaurant.management.dto.OrderRequest;
import com.restaurant.management.dto.OrderResponse;
import com.restaurant.management.entity.OrderStatus;
import com.restaurant.management.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.*;


import java.util.List;



@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {



    private final OrderService orderService;





    // CREATE ORDER
    @PreAuthorize("hasAnyRole('ADMIN','WAITER')")
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request
    ){

        return new ResponseEntity<>(

                orderService.createOrder(request),

                HttpStatus.CREATED

        );

    }





    // GET ALL ORDERS
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','WAITER')")
    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getAllOrders(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "id") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(

                orderService.getAllOrders(
                        page,
                        size,
                        sortBy,
                        direction
                )

        );

    }





    // GET ORDER BY ID
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','WAITER')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long id
    ){


        return ResponseEntity.ok(

                orderService.getOrderById(id)

        );

    }





    // GET CUSTOMER ORDERS
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','WAITER')")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> getCustomerOrders(
            @PathVariable Long customerId
    ){


        return ResponseEntity.ok(

                orderService.getCustomerOrders(customerId)

        );

    }





    // UPDATE ORDER STATUS
    @PreAuthorize("hasAnyRole('ADMIN','WAITER')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long id,

            @RequestParam OrderStatus status
    ){


        return ResponseEntity.ok(

                orderService.updateOrderStatus(
                        id,
                        status
                )

        );

    }





    // DELETE ORDER
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(
            @PathVariable Long id
    ){


        orderService.deleteOrder(id);



        return ResponseEntity.ok(
                "Order deleted successfully"
        );

    }


}

package com.restaurant.management.controller;


import com.restaurant.management.dto.BillResponse;

import com.restaurant.management.entity.BillStatus;

import com.restaurant.management.service.BillService;


import lombok.RequiredArgsConstructor;


import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class BillController {



    private final BillService billService;





    // Generate bills from order
    @PreAuthorize("hasAnyRole('ADMIN','CASHIER')")
    @PostMapping("/generate/{orderId}")
    public ResponseEntity<BillResponse> generateBill(

            @PathVariable Long orderId,

            @RequestParam(
                    required = false,
                    defaultValue = "0"
            )
            BigDecimal discount

    ){


        return ResponseEntity.ok(

                billService.generateBill(
                        orderId,
                        discount
                )

        );

    }





    @PreAuthorize("hasAnyRole('ADMIN','CASHIER')")
    @GetMapping
    public ResponseEntity<List<BillResponse>> getAllBills(){


        return ResponseEntity.ok(
                billService.getAllBills()
        );

    }





    @PreAuthorize("hasAnyRole('ADMIN','CASHIER')")
    @GetMapping("/{id}")
    public ResponseEntity<BillResponse> getBillById(

            @PathVariable Long id

    ){


        return ResponseEntity.ok(
                billService.getBillById(id)
        );

    }





    @PreAuthorize("hasAnyRole('ADMIN','CASHIER')")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<BillResponse> getByOrder(

            @PathVariable Long orderId

    ){


        return ResponseEntity.ok(
                billService.getBillByOrderId(orderId)
        );

    }





    @PreAuthorize("hasAnyRole('ADMIN','CASHIER')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<BillResponse> updateStatus(

            @PathVariable Long id,

            @RequestParam BillStatus status

    ){


        return ResponseEntity.ok(

                billService.updateBillStatus(
                        id,
                        status
                )

        );

    }


}

package com.restaurant.management.controller;


import com.restaurant.management.dto.CustomerRequest;
import com.restaurant.management.dto.CustomerResponse;

import com.restaurant.management.service.CustomerService;

import org.springframework.data.domain.Page;
import jakarta.validation.Valid;


import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.*;


import java.util.List;



@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {



    private final CustomerService customerService;





    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @Valid @RequestBody CustomerRequest request
    ){


        return new ResponseEntity<>(

                customerService.createCustomer(request),

                HttpStatus.CREATED

        );

    }





    @GetMapping
    public ResponseEntity<Page<CustomerResponse>> getAllCustomers(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "id") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(

                customerService.getAllCustomers(
                        page,
                        size,
                        sortBy,
                        direction
                )

        );

    }





    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(
            @PathVariable Long id
    ){


        return ResponseEntity.ok(
                customerService.getCustomerById(id)
        );

    }





    @GetMapping("/phone/{phone}")
    public ResponseEntity<CustomerResponse> getByPhone(
            @PathVariable String phone
    ){


        return ResponseEntity.ok(
                customerService.getCustomerByPhone(phone)
        );

    }





    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable Long id,

            @Valid @RequestBody CustomerRequest request
    ){


        return ResponseEntity.ok(
                customerService.updateCustomer(id,request)
        );

    }





    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(
            @PathVariable Long id
    ){


        customerService.deleteCustomer(id);



        return ResponseEntity.ok(
                "Customer deleted successfully"
        );

    }


}

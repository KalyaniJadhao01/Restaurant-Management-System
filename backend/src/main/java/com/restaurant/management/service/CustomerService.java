package com.restaurant.management.service;


import com.restaurant.management.dto.CustomerRequest;
import com.restaurant.management.dto.CustomerResponse;
import org.springframework.data.domain.Page;

import java.util.List;


public interface CustomerService {


    CustomerResponse createCustomer(CustomerRequest request);


    CustomerResponse updateCustomer(Long id,
                                    CustomerRequest request);


    CustomerResponse getCustomerById(Long id);


//    List<CustomerResponse> getAllCustomers();
Page<CustomerResponse> getAllCustomers(
        int page,
        int size,
        String sortBy,
        String direction
);


    CustomerResponse getCustomerByPhone(String phone);


    void deleteCustomer(Long id);

}
package com.restaurant.management.service.impl;


import com.restaurant.management.dto.CustomerRequest;
import com.restaurant.management.dto.CustomerResponse;

import com.restaurant.management.entity.Customer;

import com.restaurant.management.exception.ResourceAlreadyExistsException;
import com.restaurant.management.exception.ResourceNotFoundException;

import com.restaurant.management.repository.CustomerRepository;
import com.restaurant.management.service.CustomerService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Service;


import java.util.List;



@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {



    private final CustomerRepository customerRepository;



    @Override
    public CustomerResponse createCustomer(
            CustomerRequest request
    ) {


        if(customerRepository.existsByPhone(request.getPhone())){

            throw new ResourceAlreadyExistsException(
                    "Customer already exists with phone: "
                            + request.getPhone()
            );

        }



        if(request.getEmail()!=null &&
                customerRepository.existsByEmail(request.getEmail())){


            throw new ResourceAlreadyExistsException(
                    "Customer already exists with email: "
                            + request.getEmail()
            );

        }




        Customer customer =
                Customer.builder()

                        .name(request.getName())

                        .phone(request.getPhone())

                        .email(request.getEmail())

                        .address(request.getAddress())

                        .build();



        return mapToResponse(
                customerRepository.save(customer)
        );

    }





    @Override
    public CustomerResponse updateCustomer(
            Long id,
            CustomerRequest request
    ) {


        Customer customer =
                customerRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found with id: "
                                                + id
                                ));



        customer.setName(
                request.getName()
        );


        customer.setPhone(
                request.getPhone()
        );


        customer.setEmail(
                request.getEmail()
        );


        customer.setAddress(
                request.getAddress()
        );



        return mapToResponse(
                customerRepository.save(customer)
        );

    }





    @Override
    public CustomerResponse getCustomerById(Long id) {


        Customer customer =
                customerRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found with id: "
                                                + id
                                ));



        return mapToResponse(customer);

    }





    @Override
    public Page<CustomerResponse> getAllCustomers(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Customer> customerPage = customerRepository.findAll(pageable);

        return customerPage.map(this::mapToResponse);
    }





    @Override
    public CustomerResponse getCustomerByPhone(
            String phone
    ) {


        Customer customer =
                customerRepository.findByPhone(phone)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found with phone: "
                                                + phone
                                ));



        return mapToResponse(customer);

    }





    @Override
    public void deleteCustomer(Long id) {


        Customer customer =
                customerRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found with id: "
                                                + id
                                ));



        customerRepository.delete(customer);

    }





    private CustomerResponse mapToResponse(
            Customer customer
    ){


        return CustomerResponse.builder()

                .id(customer.getId())

                .name(customer.getName())

                .phone(customer.getPhone())

                .email(customer.getEmail())

                .address(customer.getAddress())

                .createdAt(customer.getCreatedAt())

                .updatedAt(customer.getUpdatedAt())

                .build();

    }

}
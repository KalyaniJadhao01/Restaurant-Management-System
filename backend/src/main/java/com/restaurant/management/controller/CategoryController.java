package com.restaurant.management.controller;


import com.restaurant.management.dto.CategoryRequest;
import com.restaurant.management.dto.CategoryResponse;
import com.restaurant.management.service.CategoryService;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {


    private final CategoryService categoryService;



    // CREATE CATEGORY
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest request
    ){

        CategoryResponse response =
                categoryService.createCategory(request);


        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }





    // GET ALL CATEGORIES
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','WAITER','CASHIER')")
    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> getAllCategories(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "id") String sortBy,

            @RequestParam(defaultValue = "asc") String direction

    ) {

        return ResponseEntity.ok(

                categoryService.getAllCategories(
                        page,
                        size,
                        sortBy,
                        direction
                )

        );

    }





    // GET CATEGORY BY ID
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','WAITER','CASHIER')")

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @PathVariable Long id
    ){

        return ResponseEntity.ok(
                categoryService.getCategoryById(id)
        );

    }





    // UPDATE CATEGORY
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request
    ){

        return ResponseEntity.ok(
                categoryService.updateCategory(id, request)
        );

    }





    // DELETE CATEGORY
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable Long id
    ){

        categoryService.deleteCategory(id);


        return ResponseEntity.ok(
                "Category deleted successfully"
        );

    }

}
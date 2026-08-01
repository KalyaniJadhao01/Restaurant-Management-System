package com.restaurant.management.controller;


import com.restaurant.management.dto.MenuItemRequest;
import com.restaurant.management.dto.MenuItemResponse;

import com.restaurant.management.service.MenuItemService;


import jakarta.validation.Valid;


import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.*;


import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;


@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {



    private final MenuItemService menuItemService;



    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PostMapping
    public ResponseEntity<MenuItemResponse> createMenuItem(
            @Valid @RequestBody MenuItemRequest request
    ){

        return new ResponseEntity<>(

                menuItemService.createMenuItem(request),

                HttpStatus.CREATED

        );

    }




    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','WAITER','CASHIER')")
    @GetMapping
    public ResponseEntity<Page<MenuItemResponse>> getAllMenuItems(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "200") int size,

            @RequestParam(defaultValue = "id") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(

                menuItemService.getAllMenuItems(
                        page,
                        size,
                        sortBy,
                        direction
                )

        );

    }




    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','WAITER','CASHIER')")
    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponse> getById(
            @PathVariable Long id
    ){

        return ResponseEntity.ok(
                menuItemService.getMenuItemById(id)
        );

    }




    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<MenuItemResponse> updateMenuItem(
            @PathVariable Long id,
            @Valid @RequestBody MenuItemRequest request
    ){

        return ResponseEntity.ok(
                menuItemService.updateMenuItem(id, request)
        );

    }




    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMenuItem(
            @PathVariable Long id
    ){

        menuItemService.deleteMenuItem(id);


        return ResponseEntity.ok(
                "Menu item deleted successfully"
        );

    }




    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','WAITER','CASHIER')")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<MenuItemResponse>> getByCategory(
            @PathVariable Long categoryId
    ){

        return ResponseEntity.ok(
                menuItemService
                        .getMenuItemsByCategory(categoryId)
        );

    }




    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','WAITER','CASHIER')")
    @GetMapping("/available")
    public ResponseEntity<List<MenuItemResponse>> getAvailable(){


        return ResponseEntity.ok(
                menuItemService.getAvailableItems()
        );

    }

}
package com.restaurant.management.service;


import com.restaurant.management.dto.MenuItemRequest;
import com.restaurant.management.dto.MenuItemResponse;
import org.springframework.data.domain.Page;

import java.util.List;


public interface MenuItemService {


    MenuItemResponse createMenuItem(MenuItemRequest request);


    MenuItemResponse updateMenuItem(Long id,
                                    MenuItemRequest request);


    MenuItemResponse getMenuItemById(Long id);


    Page<MenuItemResponse> getAllMenuItems(
            int page,
            int size,
            String sortBy,
            String direction
    );


    List<MenuItemResponse> getMenuItemsByCategory(Long categoryId);


    List<MenuItemResponse> getAvailableItems();


    void deleteMenuItem(Long id);

}
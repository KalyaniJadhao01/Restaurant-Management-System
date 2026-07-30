package com.restaurant.management.service.impl;


import com.restaurant.management.dto.MenuItemRequest;
import com.restaurant.management.dto.MenuItemResponse;

import com.restaurant.management.entity.Category;
import com.restaurant.management.entity.MenuItem;

import com.restaurant.management.exception.ResourceNotFoundException;

import com.restaurant.management.repository.CategoryRepository;
import com.restaurant.management.repository.MenuItemRepository;

import com.restaurant.management.service.MenuItemService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


import java.util.List;



@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {


    private final MenuItemRepository menuItemRepository;

    private final CategoryRepository categoryRepository;



    @Override
    public MenuItemResponse createMenuItem(MenuItemRequest request) {


        Category category =
                categoryRepository.findById(request.getCategoryId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found with id: "
                                                + request.getCategoryId()
                                ));



        MenuItem menuItem = MenuItem.builder()

                .name(request.getName())

                .description(request.getDescription())

                .price(request.getPrice())

                .imageUrl(request.getImageUrl())

                .available(request.getAvailable())

                .category(category)

                .build();



        return mapToResponse(
                menuItemRepository.save(menuItem)
        );

    }





    @Override
    public MenuItemResponse updateMenuItem(Long id,
                                           MenuItemRequest request) {


        MenuItem menuItem =
                menuItemRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Menu item not found with id: "
                                                + id
                                ));



        Category category =
                categoryRepository.findById(request.getCategoryId())

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found with id: "
                                                + request.getCategoryId()
                                ));




        menuItem.setName(request.getName());

        menuItem.setDescription(request.getDescription());

        menuItem.setPrice(request.getPrice());

        menuItem.setImageUrl(request.getImageUrl());

        menuItem.setAvailable(request.getAvailable());

        menuItem.setCategory(category);



        return mapToResponse(
                menuItemRepository.save(menuItem)
        );

    }





    @Override
    public MenuItemResponse getMenuItemById(Long id) {


        MenuItem menuItem =
                menuItemRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Menu item not found with id: "
                                                + id
                                ));



        return mapToResponse(menuItem);

    }





    @Override
    public Page<MenuItemResponse> getAllMenuItems(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<MenuItem> menuPage = menuItemRepository.findAll(pageable);

        return menuPage.map(this::mapToResponse);
    }





    @Override
    public List<MenuItemResponse> getMenuItemsByCategory(Long categoryId) {


        return menuItemRepository
                .findByCategoryId(categoryId)

                .stream()

                .map(this::mapToResponse)

                .toList();

    }





    @Override
    public List<MenuItemResponse> getAvailableItems() {


        return menuItemRepository
                .findByAvailableTrue()

                .stream()

                .map(this::mapToResponse)

                .toList();

    }





    @Override
    public void deleteMenuItem(Long id) {


        MenuItem menuItem =
                menuItemRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Menu item not found with id: "
                                                + id
                                ));


        menuItemRepository.delete(menuItem);

    }





    private MenuItemResponse mapToResponse(MenuItem menuItem){


        return MenuItemResponse.builder()

                .id(menuItem.getId())

                .name(menuItem.getName())

                .description(menuItem.getDescription())

                .price(menuItem.getPrice())

                .imageUrl(menuItem.getImageUrl())

                .available(menuItem.getAvailable())

                .categoryId(
                        menuItem.getCategory().getId()
                )

                .categoryName(
                        menuItem.getCategory().getName()
                )

                .createdAt(menuItem.getCreatedAt())

                .updatedAt(menuItem.getUpdatedAt())

                .build();

    }


}
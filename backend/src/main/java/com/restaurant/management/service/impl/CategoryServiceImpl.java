package com.restaurant.management.service.impl;


import com.restaurant.management.dto.CategoryRequest;
import com.restaurant.management.dto.CategoryResponse;
import com.restaurant.management.entity.Category;
import com.restaurant.management.exception.ResourceAlreadyExistsException;
import com.restaurant.management.exception.ResourceNotFoundException;
import com.restaurant.management.repository.CategoryRepository;
import com.restaurant.management.service.CategoryService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;


    @Override
    public CategoryResponse createCategory(CategoryRequest request) {


        if(categoryRepository.existsByNameIgnoreCase(request.getName())){

            throw new ResourceAlreadyExistsException(
                    "Category already exists with name: "
                            + request.getName()
            );
        }


        Category category = Category.builder()
                .name(request.getName())
                .active(true)
                .build();


        Category savedCategory = categoryRepository.save(category);


        return mapToResponse(savedCategory);
    }



    @Override
    public CategoryResponse updateCategory(Long id,
                                           CategoryRequest request) {


        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id
                        ));



        if(!category.getName()
                .equalsIgnoreCase(request.getName())
                &&
                categoryRepository.existsByNameIgnoreCase(
                        request.getName()
                )){


            throw new ResourceAlreadyExistsException(
                    "Category already exists with name: "
                            + request.getName()
            );

        }


        category.setName(request.getName());


        Category updatedCategory =
                categoryRepository.save(category);


        return mapToResponse(updatedCategory);

    }



    @Override
    public CategoryResponse getCategoryById(Long id) {


        Category category =
                categoryRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found with id: " + id
                                ));


        return mapToResponse(category);
    }



    @Override
    public Page<CategoryResponse> getAllCategories(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        return categoryPage.map(this::mapToResponse);
    }



    @Override
    public void deleteCategory(Long id) {


        Category category =
                categoryRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found with id: " + id
                                ));


        categoryRepository.delete(category);

    }



    private CategoryResponse mapToResponse(Category category){


        return CategoryResponse.builder()

                .id(category.getId())

                .name(category.getName())

                .active(category.getActive())

                .createdAt(category.getCreatedAt())

                .updatedAt(category.getUpdatedAt())

                .build();

    }

}
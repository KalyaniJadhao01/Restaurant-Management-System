package com.restaurant.management.service;

import com.restaurant.management.dto.CategoryRequest;
import com.restaurant.management.dto.CategoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {


    CategoryResponse createCategory(CategoryRequest request);


    CategoryResponse updateCategory(Long id, CategoryRequest request);


    CategoryResponse getCategoryById(Long id);


//    List<CategoryResponse> getAllCategories();
     Page<CategoryResponse> getAllCategories(
        int page,
        int size,
        String sortBy,
        String direction
     );


    void deleteCategory(Long id);

}
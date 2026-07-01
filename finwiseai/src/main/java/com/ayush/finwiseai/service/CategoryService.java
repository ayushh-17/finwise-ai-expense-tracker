package com.ayush.finwiseai.service;

import com.ayush.finwiseai.dto.request.CategoryRequest;
import com.ayush.finwiseai.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    List<CategoryResponse> getAllCategories();
    CategoryResponse getCategoryById(Long id);
    void deleteCategory(Long id);
}
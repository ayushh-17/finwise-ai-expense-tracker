package com.ayush.finwiseai.service;

import com.ayush.finwiseai.dto.request.CategoryRequest;
import com.ayush.finwiseai.dto.response.CategoryResponse;
import com.ayush.finwiseai.entity.Category;
import com.ayush.finwiseai.exception.ResourceNotFoundException;
import com.ayush.finwiseai.repository.CategoryRepository;
import com.ayush.finwiseai.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Food");
        category.setDefault(false);
    }

    @Test
    void createCategory_ShouldReturnSavedCategory() {
        // Arrange
        CategoryRequest request = new CategoryRequest();
        request.setName("Food");

        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // Act
        CategoryResponse response = categoryService.createCategory(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Food");
        assertThat(response.getId()).isEqualTo(1L);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void getCategoryById_WhenCategoryExists_ShouldReturnCategory() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // Act
        CategoryResponse response = categoryService.getCategoryById(1L);

        // Assert
        assertThat(response.getName()).isEqualTo("Food");
    }

    @Test
    void getCategoryById_WhenCategoryDoesNotExist_ShouldThrowException() {
        // Arrange
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> categoryService.getCategoryById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found with id: 99");
    }

    @Test
    void getAllCategories_ShouldReturnListOfCategories() {
        // Arrange
        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Travel");

        when(categoryRepository.findAll()).thenReturn(List.of(category, category2));

        // Act
        List<CategoryResponse> responses = categoryService.getAllCategories();

        // Assert
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getName()).isEqualTo("Food");
        assertThat(responses.get(1).getName()).isEqualTo("Travel");
    }

    @Test
    void deleteCategory_WhenCategoryExists_ShouldDeleteSuccessfully() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(category);

        // Act
        categoryService.deleteCategory(1L);

        // Assert
        verify(categoryRepository, times(1)).delete(category);
    }
}
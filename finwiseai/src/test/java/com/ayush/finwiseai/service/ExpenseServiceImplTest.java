package com.ayush.finwiseai.service;

import com.ayush.finwiseai.dto.request.ExpenseRequest;
import com.ayush.finwiseai.dto.response.ExpenseResponse;
import com.ayush.finwiseai.entity.Category;
import com.ayush.finwiseai.entity.Expense;
import com.ayush.finwiseai.entity.User;
import com.ayush.finwiseai.exception.ResourceNotFoundException;
import com.ayush.finwiseai.repository.CategoryRepository;
import com.ayush.finwiseai.repository.ExpenseRepository;
import com.ayush.finwiseai.service.impl.ExpenseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceImplTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    private User userA;
    private User userB;
    private Category category;
    private Expense expense;

    @BeforeEach
    void setUp() {
        userA = new User();
        userA.setId(1L);
        userA.setName("User A");
        userA.setEmail("usera@test.com");

        userB = new User();
        userB.setId(2L);
        userB.setName("User B");
        userB.setEmail("userb@test.com");

        category = new Category();
        category.setId(1L);
        category.setName("Food");

        expense = new Expense();
        expense.setId(1L);
        expense.setUser(userA);
        expense.setCategory(category);
        expense.setAmount(500.0);
        expense.setDescription("Swiggy order");
        expense.setExpenseDate(LocalDate.of(2026, 7, 1));
    }

    @Test
    void createExpense_ShouldReturnCreatedExpense() {
        // Arrange
        ExpenseRequest request = new ExpenseRequest();
        request.setCategoryId(1L);
        request.setAmount(500.0);
        request.setDescription("Swiggy order");
        request.setExpenseDate(LocalDate.of(2026, 7, 1));

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

        // Act
        ExpenseResponse response = expenseService.createExpense(request, userA);

        // Assert
        assertThat(response.getAmount()).isEqualTo(500.0);
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getCategoryName()).isEqualTo("Food");
    }

    @Test
    void createExpense_WhenCategoryDoesNotExist_ShouldThrowException() {
        // Arrange
        ExpenseRequest request = new ExpenseRequest();
        request.setCategoryId(99L);
        request.setAmount(500.0);
        request.setExpenseDate(LocalDate.now());

        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> expenseService.createExpense(request, userA))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found with id: 99");
    }

    @Test
    void getExpenseById_WhenUserIsOwner_ShouldReturnExpense() {
        // Arrange
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));

        // Act
        ExpenseResponse response = expenseService.getExpenseById(1L, userA);

        // Assert
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getDescription()).isEqualTo("Swiggy order");
    }

    @Test
    void getExpenseById_WhenUserIsNotOwner_ShouldThrowAccessDeniedException() {
        // Arrange - expense belongs to userA, but userB is trying to access it
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));

        // Act & Assert
        assertThatThrownBy(() -> expenseService.getExpenseById(1L, userB))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("You are not allowed to access this expense");
    }

    @Test
    void deleteExpense_WhenUserIsNotOwner_ShouldThrowAccessDeniedException() {
        // Arrange
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));

        // Act & Assert
        assertThatThrownBy(() -> expenseService.deleteExpense(1L, userB))
                .isInstanceOf(AccessDeniedException.class);

        // Verify delete was NEVER called since access was denied
        verify(expenseRepository, never()).delete(any(Expense.class));
    }

    @Test
    void getExpenseById_WhenExpenseDoesNotExist_ShouldThrowException() {
        // Arrange
        when(expenseRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> expenseService.getExpenseById(99L, userA))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Expense not found with id: 99");
    }
}
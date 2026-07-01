package com.ayush.finwiseai.service.impl;

import com.ayush.finwiseai.dto.request.ExpenseRequest;
import com.ayush.finwiseai.dto.response.ExpenseResponse;
import com.ayush.finwiseai.entity.Category;
import com.ayush.finwiseai.entity.Expense;
import com.ayush.finwiseai.entity.User;
import com.ayush.finwiseai.exception.ResourceNotFoundException;
import com.ayush.finwiseai.repository.CategoryRepository;
import com.ayush.finwiseai.repository.ExpenseRepository;
import com.ayush.finwiseai.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ExpenseResponse createExpense(ExpenseRequest request, User currentUser) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        Expense expense = new Expense();
        expense.setUser(currentUser);
        expense.setCategory(category);
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setExpenseDate(request.getExpenseDate());

        Expense saved = expenseRepository.save(expense);
        return mapToResponse(saved);
    }

    @Override
    public List<ExpenseResponse> getMyExpenses(User currentUser) {
        return expenseRepository.findByUserId(currentUser.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ExpenseResponse getExpenseById(Long id, User currentUser) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));

        checkOwnership(expense, currentUser);
        return mapToResponse(expense);
    }

    @Override
    public ExpenseResponse updateExpense(Long id, ExpenseRequest request, User currentUser) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));

        checkOwnership(expense, currentUser);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        expense.setCategory(category);
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setExpenseDate(request.getExpenseDate());

        Expense updated = expenseRepository.save(expense);
        return mapToResponse(updated);
    }

    @Override
    public void deleteExpense(Long id, User currentUser) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));

        checkOwnership(expense, currentUser);
        expenseRepository.delete(expense);
    }

    private void checkOwnership(Expense expense, User currentUser) {
        if (!expense.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to access this expense");
        }
    }

    private ExpenseResponse mapToResponse(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getUser().getId(),
                expense.getUser().getName(),
                expense.getCategory().getId(),
                expense.getCategory().getName(),
                expense.getAmount(),
                expense.getDescription(),
                expense.getExpenseDate(),
                expense.getCreatedAt()
        );
    }
}
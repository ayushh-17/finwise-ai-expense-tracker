package com.ayush.finwiseai.service;

import com.ayush.finwiseai.dto.request.ExpenseRequest;
import com.ayush.finwiseai.dto.response.ExpenseResponse;
import com.ayush.finwiseai.entity.User;

import java.util.List;

public interface ExpenseService {
    ExpenseResponse createExpense(ExpenseRequest request, User currentUser);
    List<ExpenseResponse> getMyExpenses(User currentUser);
    ExpenseResponse getExpenseById(Long id, User currentUser);
    ExpenseResponse updateExpense(Long id, ExpenseRequest request, User currentUser);
    void deleteExpense(Long id, User currentUser);
}
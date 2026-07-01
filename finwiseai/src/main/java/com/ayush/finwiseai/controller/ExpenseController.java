package com.ayush.finwiseai.controller;

import com.ayush.finwiseai.dto.request.ExpenseRequest;
import com.ayush.finwiseai.dto.response.ExpenseResponse;
import com.ayush.finwiseai.entity.User;
import com.ayush.finwiseai.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(
            @Valid @RequestBody ExpenseRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        ExpenseResponse response = expenseService.createExpense(request, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getMyExpenses(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(expenseService.getMyExpenses(currentUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(expenseService.getExpenseById(id, currentUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(expenseService.updateExpense(id, request, currentUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {
        expenseService.deleteExpense(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
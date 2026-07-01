package com.ayush.finwiseai.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseRequest {

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Amount is required")
    private Double amount;

    private String description;

    @NotNull(message = "Expense date is required")
    private LocalDate expenseDate;
}
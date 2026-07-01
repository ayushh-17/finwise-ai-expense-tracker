package com.ayush.finwiseai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponse {

    private Long id;
    private Long userId;
    private String userName;
    private Long categoryId;
    private String categoryName;
    private Double amount;
    private String description;
    private LocalDate expenseDate;
    private LocalDateTime createdAt;
}
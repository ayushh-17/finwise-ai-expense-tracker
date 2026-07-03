package com.ayush.finwiseai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedExpenseResponse {

    private List<ExpenseResponse> expenses;
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private boolean isLastPage;
}
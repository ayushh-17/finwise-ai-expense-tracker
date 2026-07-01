package com.ayush.finwiseai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlySummaryResponse {

    private int month;
    private int year;
    private Double totalSpent;
    private List<CategoryBreakdown> categoryBreakdown;
}
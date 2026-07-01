package com.ayush.finwiseai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryBreakdown {

    private String categoryName;
    private Double totalAmount;
}
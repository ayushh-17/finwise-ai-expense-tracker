package com.ayush.finwiseai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorizeResponse {

    private String suggestedCategory;
    private double confidence;  // 0.0 to 1.0 - kitna confident hai suggestion
}
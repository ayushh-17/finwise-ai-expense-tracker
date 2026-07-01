package com.ayush.finwiseai.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategorizeRequest {

    @NotBlank(message = "Description is required")
    private String description;
}
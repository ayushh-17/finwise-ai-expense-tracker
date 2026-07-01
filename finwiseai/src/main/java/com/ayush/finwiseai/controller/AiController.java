package com.ayush.finwiseai.controller;

import com.ayush.finwiseai.dto.request.CategorizeRequest;
import com.ayush.finwiseai.dto.response.CategorizeResponse;
import com.ayush.finwiseai.dto.response.InsightsResponse;
import com.ayush.finwiseai.dto.response.MonthlySummaryResponse;
import com.ayush.finwiseai.entity.User;
import com.ayush.finwiseai.service.AiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/categorize")
    public ResponseEntity<CategorizeResponse> categorize(@Valid @RequestBody CategorizeRequest request) {
        return ResponseEntity.ok(aiService.categorize(request));
    }

    @GetMapping("/monthly-summary")
    public ResponseEntity<MonthlySummaryResponse> getMonthlySummary(
            @RequestParam int month,
            @RequestParam int year,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(aiService.getMonthlySummary(currentUser, month, year));
    }

    @GetMapping("/insights")
    public ResponseEntity<InsightsResponse> getInsights(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(aiService.getInsights(currentUser));
    }
}
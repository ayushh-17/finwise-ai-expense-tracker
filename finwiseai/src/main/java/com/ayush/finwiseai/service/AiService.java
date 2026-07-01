package com.ayush.finwiseai.service;

import com.ayush.finwiseai.dto.request.CategorizeRequest;
import com.ayush.finwiseai.dto.response.CategorizeResponse;
import com.ayush.finwiseai.dto.response.InsightsResponse;
import com.ayush.finwiseai.dto.response.MonthlySummaryResponse;
import com.ayush.finwiseai.entity.User;

public interface AiService {
    CategorizeResponse categorize(CategorizeRequest request);
    MonthlySummaryResponse getMonthlySummary(User currentUser, int month, int year);
    InsightsResponse getInsights(User currentUser);
}
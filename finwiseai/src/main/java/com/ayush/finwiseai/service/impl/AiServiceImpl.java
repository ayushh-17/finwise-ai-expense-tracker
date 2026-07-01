package com.ayush.finwiseai.service.impl;

import com.ayush.finwiseai.dto.request.CategorizeRequest;
import com.ayush.finwiseai.dto.response.CategorizeResponse;
import com.ayush.finwiseai.dto.response.CategoryBreakdown;
import com.ayush.finwiseai.dto.response.InsightsResponse;
import com.ayush.finwiseai.dto.response.MonthlySummaryResponse;
import com.ayush.finwiseai.entity.Expense;
import com.ayush.finwiseai.entity.User;
import com.ayush.finwiseai.repository.ExpenseRepository;
import com.ayush.finwiseai.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final ExpenseRepository expenseRepository;

    private static final Map<String, List<String>> CATEGORY_KEYWORDS = Map.of(
            "Food", List.of("swiggy", "zomato", "restaurant", "food", "lunch", "dinner", "breakfast", "cafe", "pizza", "burger"),
            "Travel", List.of("uber", "ola", "cab", "taxi", "flight", "train", "bus", "petrol", "fuel", "irctc"),
            "Shopping", List.of("amazon", "flipkart", "myntra", "shopping", "mall", "store"),
            "Entertainment", List.of("movie", "netflix", "spotify", "prime", "bookmyshow", "cinema", "concert"),
            "Bills", List.of("electricity", "bill", "recharge", "wifi", "internet", "mobile", "rent"),
            "Health", List.of("medicine", "hospital", "doctor", "pharmacy", "medical", "clinic")
    );

    @Override
    public CategorizeResponse categorize(CategorizeRequest request) {
        String description = request.getDescription().toLowerCase();

        for (Map.Entry<String, List<String>> entry : CATEGORY_KEYWORDS.entrySet()) {
            String category = entry.getKey();
            List<String> keywords = entry.getValue();

            for (String keyword : keywords) {
                if (description.contains(keyword)) {
                    return new CategorizeResponse(category, 0.85);
                }
            }
        }

        return new CategorizeResponse("Others", 0.3);
    }

    @Override
    public MonthlySummaryResponse getMonthlySummary(User currentUser, int month, int year) {

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Expense> expenses = expenseRepository.findByUserIdAndDateRange(
                currentUser.getId(), startDate, endDate
        );

        double totalSpent = expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        Map<String, Double> categoryTotals = expenses.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getCategory().getName(),
                        Collectors.summingDouble(Expense::getAmount)
                ));

        List<CategoryBreakdown> breakdown = categoryTotals.entrySet().stream()
                .map(entry -> new CategoryBreakdown(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return new MonthlySummaryResponse(month, year, totalSpent, breakdown);
    }
    @Override
    public InsightsResponse getInsights(User currentUser) {

        List<String> insights = new ArrayList<>();

        LocalDate today = LocalDate.now();
        YearMonth currentMonth = YearMonth.from(today);
        YearMonth previousMonth = currentMonth.minusMonths(1);

        MonthlySummaryResponse currentSummary = getMonthlySummary(
                currentUser, currentMonth.getMonthValue(), currentMonth.getYear()
        );
        MonthlySummaryResponse previousSummary = getMonthlySummary(
                currentUser, previousMonth.getMonthValue(), previousMonth.getYear()
        );

        // Insight 1: Total spending is month
        if (currentSummary.getTotalSpent() > 0) {
            insights.add(String.format(
                    "You've spent ₹%.2f so far this month.",
                    currentSummary.getTotalSpent()
            ));
        } else {
            insights.add("No expenses recorded yet this month. Start tracking to get insights!");
        }

        // Insight 2: Comparison with previous month
        if (previousSummary.getTotalSpent() > 0 && currentSummary.getTotalSpent() > 0) {
            double percentChange = ((currentSummary.getTotalSpent() - previousSummary.getTotalSpent())
                    / previousSummary.getTotalSpent()) * 100;

            if (percentChange > 10) {
                insights.add(String.format(
                        "⚠️ You're spending %.1f%% more than last month. Consider reviewing your budget.",
                        percentChange
                ));
            } else if (percentChange < -10) {
                insights.add(String.format(
                        "✅ Great job! You're spending %.1f%% less than last month.",
                        Math.abs(percentChange)
                ));
            } else {
                insights.add("Your spending is about the same as last month.");
            }
        }

        // Insight 3: Highest spending category
        if (!currentSummary.getCategoryBreakdown().isEmpty()) {
            CategoryBreakdown topCategory = currentSummary.getCategoryBreakdown().stream()
                    .max((a, b) -> Double.compare(a.getTotalAmount(), b.getTotalAmount()))
                    .orElse(null);

            if (topCategory != null) {
                insights.add(String.format(
                        "Your highest spending category this month is %s (₹%.2f).",
                        topCategory.getCategoryName(), topCategory.getTotalAmount()
                ));
            }
        }

        return new InsightsResponse(insights);
    }
}
package com.codeleb.insights.domain.service;

import com.codeleb.insights.domain.model.ActionableInsight;
import com.codeleb.insights.domain.model.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Purpose: Implements the PredictionStrategy using a moving average algorithmic
 * approach.
 * Design Patterns: Strategy Pattern implementation.
 * Expected Inputs/Outputs: Series of transactions as input, moving average
 * ActionableInsights as output grouped by customer and category.
 * Strict Constraints: Pure Java class. No framework annotations. Constructor
 * injection only for dependencies, if any.
 */
public class MovingAverageStrategy implements PredictionStrategy {

    private final int windowSize;

    public MovingAverageStrategy(int windowSize) {
        if (windowSize <= 0) {
            throw new IllegalArgumentException("Window size must be greater than zero");
        }
        this.windowSize = windowSize;
    }

    @Override
    public List<ActionableInsight> predict(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return Collections.emptyList();
        }

        // Group transactions by customerId and then by category
        Map<String, Map<String, List<Transaction>>> groupedTransactions = transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::customerId,
                        Collectors.groupingBy(Transaction::category)));

        List<ActionableInsight> insights = new ArrayList<>();

        // Calculate moving average for each customer's category
        for (Map.Entry<String, Map<String, List<Transaction>>> customerEntry : groupedTransactions.entrySet()) {
            String customerId = customerEntry.getKey();

            for (Map.Entry<String, List<Transaction>> categoryEntry : customerEntry.getValue().entrySet()) {
                String category = categoryEntry.getKey();
                List<Transaction> customerCategoryTransactions = categoryEntry.getValue();

                insights.add(calculateMovingAverage(customerId, category, customerCategoryTransactions));
            }
        }

        return insights;
    }

    private ActionableInsight calculateMovingAverage(String customerId, String category,
            List<Transaction> transactions) {
        int size = transactions.size();
        int elementsToConsider = Math.min(size, windowSize);

        // Take the most recent transactions based on the window size
        List<Transaction> recentTransactions = transactions.subList(size - elementsToConsider, size);

        BigDecimal sum = recentTransactions.stream()
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate the average with scale 2
        BigDecimal average = sum.divide(BigDecimal.valueOf(elementsToConsider), 2, RoundingMode.HALF_UP);

        // Confidence logic: 1.0 (100%) if we have enough items to fill the window,
        // linearly less if we have fewer.
        double confidenceScore = (double) elementsToConsider / windowSize;
        // round to 2 decimal places exactly as precision in assertj double tuple
        // behaves predictably
        confidenceScore = Math.round(confidenceScore * 100.0) / 100.0;

        String type = "MOVING_AVERAGE_" + category.toUpperCase();
        String description = String.format("Calculated moving average for %s over a window of %d transactions",
                category, windowSize);

        return new ActionableInsight(
                customerId,
                type,
                description,
                average,
                confidenceScore);
    }
}

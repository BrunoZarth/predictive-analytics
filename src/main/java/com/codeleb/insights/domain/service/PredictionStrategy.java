package com.codeleb.insights.domain.service;

import com.codeleb.insights.domain.model.ActionableInsight;
import com.codeleb.insights.domain.model.Transaction;

import java.util.List;

/**
 * Purpose: Defines the contract for applying predictive analysis on
 * transactions.
 * Design Patterns: Strategy Pattern.
 * Expected Inputs/Outputs: Takes transaction data as input, returns a list of
 * ActionableInsights grouped by logic.
 * Strict Constraints: Pure Java interface. No Spring @Component or @Service
 * annotations.
 */
public interface PredictionStrategy {

    /**
     * Calculates a prediction or insight based on a list of transactions.
     *
     * @param transactions The input list of transactions.
     * @return A list of ActionableInsights encapsulating the predicted results per
     *         customer/category.
     */
    List<ActionableInsight> predict(List<Transaction> transactions);
}

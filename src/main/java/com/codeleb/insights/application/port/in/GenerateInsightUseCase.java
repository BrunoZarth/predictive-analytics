package com.codeleb.insights.application.port.in;

import com.codeleb.insights.domain.model.ActionableInsight;

import java.util.List;

/**
 * Inbound Port: Contract for generating insights for a specific tenant.
 */
public interface GenerateInsightUseCase {

    /**
     * Executes predictive strategies specifically bounded by customerId.
     * 
     * @param customerId The multi-tenant isolation key
     * @return List of generated ActionableInsights for this customer
     */
    List<ActionableInsight> generateForCustomer(String customerId);
}

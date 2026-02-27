package com.codeleb.insights.domain.model;

import com.codeleb.insights.domain.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ActionableInsightTest {

    @Test
    void givenValidData_whenCreatingActionableInsight_thenSuccess() {
        // Arrange
        String customerId = "cust_999";
        String type = "MOVING_AVERAGE_FOOD";
        String description = "Calculated average over window";
        BigDecimal value = new BigDecimal("100.00");
        double confidenceScore = 0.85;

        // Act
        ActionableInsight insight = new ActionableInsight(customerId, type, description, value, confidenceScore);

        // Assert
        assertThat(insight).isNotNull();
        assertThat(insight.customerId()).isEqualTo(customerId);
        assertThat(insight.type()).isEqualTo(type);
        assertThat(insight.description()).isEqualTo(description);
        assertThat(insight.value()).isEqualTo(value);
        assertThat(insight.confidenceScore()).isEqualTo(confidenceScore);
    }

    @Test
    void givenMissingCustomerId_whenCreatingActionableInsight_thenThrowsBusinessRuleViolationException() {
        // Arrange
        String blankCustomerId = "   ";

        // Act & Assert
        assertThatThrownBy(() -> new ActionableInsight(blankCustomerId, "TYPE", "desc", BigDecimal.TEN, 1.0))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("customerId");
    }

    @Test
    void givenInvalidConfidenceScore_whenCreatingActionableInsight_thenThrowsBusinessRuleViolationException() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new ActionableInsight("cust_1", "TYPE", "desc", BigDecimal.TEN, -0.1))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("confidenceScore");

        assertThatThrownBy(() -> new ActionableInsight("cust_1", "TYPE", "desc", BigDecimal.TEN, 1.1))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("confidenceScore");
    }
}

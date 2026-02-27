package com.codeleb.insights.domain.service;

import com.codeleb.insights.domain.model.ActionableInsight;
import com.codeleb.insights.domain.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class MovingAverageStrategyTest {

        private MovingAverageStrategy movingAverageStrategy;

        @BeforeEach
        void setUp() {
                // Initialize strategy with a moving average window size of 3
                movingAverageStrategy = new MovingAverageStrategy(3);
        }

        @Test
        void givenValidTransactionsOfMixedCustomersAndCategories_whenPredict_thenGroupsAndCalculatesSeparately() {
                // Arrange
                List<Transaction> transactions = List.of(
                                // Customer 1 - FOOD (Window size 3 means last 3 items) -> 10, 20, 30. Avg =
                                // 20.00
                                new Transaction("t1", "1", "FOOD", new BigDecimal("5.00"), "USD", LocalDateTime.now()),
                                new Transaction("t2", "1", "FOOD", new BigDecimal("10.00"), "USD",
                                                LocalDateTime.now().plusHours(1)),
                                new Transaction("t3", "1", "FOOD", new BigDecimal("20.00"), "USD",
                                                LocalDateTime.now().plusHours(2)),
                                new Transaction("t4", "1", "FOOD", new BigDecimal("30.00"), "USD",
                                                LocalDateTime.now().plusHours(3)),

                                // Customer 1 - SOFTWARE (Window size 3 but only 2 items) -> 100, 200. Avg =
                                // 150.00
                                new Transaction("t5", "1", "SOFTWARE", new BigDecimal("100.00"), "USD",
                                                LocalDateTime.now()),
                                new Transaction("t6", "1", "SOFTWARE", new BigDecimal("200.00"), "USD",
                                                LocalDateTime.now().plusHours(1)),

                                // Customer 2 - FOOD -> Avg = 50.00
                                new Transaction("t7", "2", "FOOD", new BigDecimal("50.00"), "USD",
                                                LocalDateTime.now()));

                // Act
                List<ActionableInsight> insights = movingAverageStrategy.predict(transactions);

                // Assert
                assertThat(insights).isNotNull().hasSize(3);

                assertThat(insights)
                                .extracting(ActionableInsight::customerId, ActionableInsight::type,
                                                ActionableInsight::value,
                                                ActionableInsight::confidenceScore)
                                .containsExactlyInAnyOrder(
                                                tuple("1", "MOVING_AVERAGE_FOOD", new BigDecimal("20.00"), 1.0), // 3
                                                                                                                 // items
                                                                                                                 // filled
                                                                                                                 // the
                                                                                                                 // window
                                                                                                                 // completely
                                                tuple("1", "MOVING_AVERAGE_SOFTWARE", new BigDecimal("150.00"), 0.67), // 2
                                                                                                                       // items
                                                                                                                       // out
                                                                                                                       // of
                                                                                                                       // 3
                                                                                                                       // window
                                                tuple("2", "MOVING_AVERAGE_FOOD", new BigDecimal("50.00"), 0.33) // 1
                                                                                                                 // item
                                                                                                                 // out
                                                                                                                 // of 3
                                                                                                                 // window
                                );
        }

        @Test
        void givenEmptyTransactionList_whenPredict_thenReturnsEmptyList() {
                // Arrange
                List<Transaction> transactions = Collections.emptyList();

                // Act
                List<ActionableInsight> insights = movingAverageStrategy.predict(transactions);

                // Assert
                assertThat(insights).isNotNull().isEmpty();
        }
}

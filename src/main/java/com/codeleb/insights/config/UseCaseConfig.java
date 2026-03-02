package com.codeleb.insights.config;

import com.codeleb.insights.application.port.out.LoadTransactionPort;
import com.codeleb.insights.application.port.out.SaveTransactionPort;
import com.codeleb.insights.application.service.TransactionFacadeService;
import com.codeleb.insights.domain.service.MovingAverageStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public TransactionFacadeService transactionFacadeService(
            SaveTransactionPort saveTransactionPort,
            LoadTransactionPort loadTransactionPort) {

        // Wire the pure Java Domain Strategy with a default window size of 5
        MovingAverageStrategy movingAverageStrategy = new MovingAverageStrategy(5);

        // Instantiate the Application Service manually
        return new TransactionFacadeService(saveTransactionPort, loadTransactionPort, movingAverageStrategy);
    }
}

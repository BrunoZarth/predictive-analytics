package com.codeleb.insights.adapter.out.persistence;

/**
 * Purpose: Implements outbound ports (LoadTransactionPort, SaveTransactionPort)
 * bridging domain/application and DB.
 * Design Patterns: Adapter (Hexagonal Architecture), Repository Implementor.
 * Expected Inputs/Outputs: Maps Domain Models to Entities and delegates to
 * JpaTransactionRepository.
 * Strict Constraints: Implements application Outbound Ports. @Component
 * allowed. Constructor injection ONLY.
 */
public class TransactionPersistenceAdapter {
}

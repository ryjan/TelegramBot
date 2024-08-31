package org.ryjan.telegram.repos;

import org.ryjan.telegram.model.BankDatabase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankDatabaseRepository extends JpaRepository<BankDatabase, Long> {
}

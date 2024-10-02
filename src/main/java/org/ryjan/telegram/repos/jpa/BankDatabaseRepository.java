package org.ryjan.telegram.repos.jpa;

import org.ryjan.telegram.model.users.BankDatabase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankDatabaseRepository extends JpaRepository<BankDatabase, Long> {
}

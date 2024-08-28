package org.ryjan.telegram.repos;

import org.ryjan.telegram.domain.BankDatabase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface BankDatabaseRepository extends JpaRepository<BankDatabase, Long> {
}

package org.ryjan.telegram.interfaces.repos.jpa;

import org.ryjan.telegram.model.users.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Long> {
}

package org.ryjan.telegram.interfaces.repos.redis;

import org.ryjan.telegram.model.users.Bank;
import org.springframework.data.repository.CrudRepository;

public interface BankRedisRepository extends CrudRepository<Bank, Long> {
}

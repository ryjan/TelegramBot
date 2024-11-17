package org.ryjan.telegram.interfaces.repos.redis;

import org.ryjan.telegram.model.users.Bank;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.repository.CrudRepository;

@RedisHash("Bank")
public interface RedisBankDatabaseRepository extends CrudRepository<Bank, Long> {

}

package org.ryjan.telegram.interfaces.repos.redis;

import org.ryjan.telegram.model.users.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRedisRepository extends CrudRepository<User, Long> {
}

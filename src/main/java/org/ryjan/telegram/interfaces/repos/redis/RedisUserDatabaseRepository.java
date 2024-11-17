package org.ryjan.telegram.interfaces.repos.redis;

import org.ryjan.telegram.model.users.User;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.repository.CrudRepository;

@RedisHash("User")
public interface RedisUserDatabaseRepository extends CrudRepository<User, Long> {
    User findByUserTag(String tag);

    Boolean existsByUserTag(String username);
}

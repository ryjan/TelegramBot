package org.ryjan.telegram.repos.redis;

import org.ryjan.telegram.model.users.UserDatabase;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.repository.CrudRepository;
import org.telegram.telegrambots.meta.api.objects.User;

@RedisHash("User")
public interface RedisUserDatabaseRepository extends CrudRepository<UserDatabase, String> {
    UserDatabase findByUserTag(String tag);

    Boolean existsByUserTag(String username);
}

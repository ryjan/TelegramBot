package org.ryjan.telegram.services;

import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.interfaces.Permissions;
import org.ryjan.telegram.model.users.UserDatabase;
import org.ryjan.telegram.interfaces.repos.jpa.BankDatabaseRepository;
import org.ryjan.telegram.interfaces.repos.jpa.JpaUserDatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Permission;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    public final String CACHE_KEY = "userDatabase:";

    @Autowired
    private JpaUserDatabaseRepository userDatabaseRepository;

    @Autowired
    RedisTemplate<String, UserDatabase> redisTemplate;

    @Autowired
    private BankDatabaseRepository bankDatabaseRepository;

    @Transactional
    public UserDatabase createUser(Long id, String username) {
        UserDatabase userDatabase = new UserDatabase(id, username);
        return userDatabaseRepository.save(userDatabase);
    }

    public UserDatabase findUser(String username) {
        return userDatabaseRepository.findByUserTag(username);
    }

    public UserDatabase findUser(Long id) {
        return userDatabaseRepository.findById(id).orElse(null);
    }

    public Boolean hasPermission(Long userId, Permissions permissions) {
        if (permissions == UserPermissions.ANY || permissions == GroupPermissions.ANY) return true;

        UserDatabase userDatabase = redisTemplate.opsForValue().get(CACHE_KEY + userId);

        if (userDatabase == null) {
            userDatabase = findUser(userId);
            redisTemplate.opsForValue().set(CACHE_KEY + userId, userDatabase, 1, TimeUnit.HOURS);
        }
        return userDatabase != null && userDatabase.getUserGroup().isAtLeast(permissions);
    }

    public Boolean userIsExist(Long id) {
        return userDatabaseRepository.existsById(id);
    }

    public Boolean userIsExist(String username) {
        return userDatabaseRepository.existsByUserTag(username);
    }

    public void update(UserDatabase userDatabase) {
        userDatabaseRepository.save(userDatabase);
    }

    public void delete(UserDatabase userDatabase) {
        userDatabaseRepository.delete(userDatabase);
    }
}

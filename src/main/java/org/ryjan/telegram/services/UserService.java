package org.ryjan.telegram.services;

import org.ryjan.telegram.interfaces.Permissions;
import org.ryjan.telegram.model.users.UserDatabase;
import org.ryjan.telegram.interfaces.repos.jpa.BankDatabaseRepository;
import org.ryjan.telegram.interfaces.repos.jpa.JpaUserDatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Permission;

@Service
public class UserService {
    private final String CACHE_KEY = "userPermission:";

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
        UserDatabase userDatabase = redisTemplate.opsForValue().get(CACHE_KEY + userId);

        if (userDatabase == null) {
            userDatabase = findUser(userId);
            redisTemplate.opsForValue().set(CACHE_KEY + userId, userDatabase);
        }
        return userDatabase != null && userDatabase.getUserGroup().equals(permissions.getName());
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

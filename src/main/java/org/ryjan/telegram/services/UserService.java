package org.ryjan.telegram.services;

import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.interfaces.Permissions;
import org.ryjan.telegram.kafka.UserProducer;
import org.ryjan.telegram.model.users.User;
import org.ryjan.telegram.interfaces.repos.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    public final String CACHE_KEY = "userDatabase:";

    @Autowired
    private UserProducer userProducer;

    @Autowired
    private RedisTemplate<String, User> redisTemplate;

    @Autowired
    private UserRepository userDatabaseRepository;

    public void processAndSendUser(User user) {
        userProducer.sendUser(user);
    }

    public User findUser(String usernameOrId) {
        if (isNumeric(usernameOrId)) {
            return userDatabaseRepository.findById(Long.parseLong(usernameOrId)).orElse(null);
        }
        return userDatabaseRepository.findByUsername(usernameOrId);
    }

    public User findUser(Long id) {
        return userDatabaseRepository.findById(id).orElse(null);
    }

    public Boolean hasPermission(Long userId, Permissions permissions) {
        if (permissions == UserPermissions.ANY || permissions == GroupPermissions.ANY) return true;

        User user = redisTemplate.opsForValue().get(CACHE_KEY + userId);

        if (user == null) {
            user = findUser(userId);
            redisTemplate.opsForValue().set(CACHE_KEY + userId, user, 1, TimeUnit.HOURS);
        }
        return user != null && user.getUserGroup().isAtLeast(permissions);
    }

    public Boolean userIsExist(Long id) {
        return userDatabaseRepository.existsById(id);
    }

    public Boolean userIsExist(String username) {
        return userDatabaseRepository.existsByUsername(username);
    }

    public void update(User user) {
        userDatabaseRepository.save(user);
    }

    public void saveAll(List<User> users) {
        userDatabaseRepository.saveAll(users);
    }

    public void delete(User user) {
        userDatabaseRepository.delete(user);
    }

    private boolean isNumeric(String string) {
        try {
            Long.parseLong(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

package org.ryjan.telegram.services;

import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.config.RedisConfig;
import org.ryjan.telegram.interfaces.Permissions;
import org.ryjan.telegram.kafka.UserProducer;
import org.ryjan.telegram.model.users.User;
import org.ryjan.telegram.interfaces.repos.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class UserService {
    public final String CACHE_KEY = RedisConfig.USER_CACHE_KEY;

    @Autowired
    private UserProducer userProducer;

    @Autowired
    private RedisTemplate<String, User> userRedisTemplate;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User createUser(User user) {
        if (findUser(user.getId()) != null) {
            return findUser(user.getId());
        }
        return userRepository.save(user);
    }

    public User findUser(String usernameOrId) {
        if (isNumeric(usernameOrId)) {
            return findUser(Long.parseLong(usernameOrId));
        }
        return userRepository.findByUsername(usernameOrId);
    }

    public User findUser(Long id) {
        User user = userRedisTemplate.opsForValue().get(CACHE_KEY + id);

        if (user == null) {
            CompletableFuture<Void> future = userProducer.findUser(id);

            try {
                future.get(1, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new RuntimeException("Failed to fetch user from Kafka", e);
            }

            user = userRedisTemplate.opsForValue().get(CACHE_KEY + id);
        }
        return user;
    }

    public void setUserInRedis(User user) {
        userRedisTemplate.opsForValue().set(CACHE_KEY + user.getId(), user, 10, TimeUnit.MINUTES);
    }

    public Boolean hasRequiredPermission(Long userId, Permissions requiredPermission) {
        if (requiredPermission == UserPermissions.ANY || requiredPermission == GroupPermissions.ANY) return true;
        User user = findUser(userId);
        return user != null && user.getUserGroup().isAtLeast(requiredPermission);
    }

    public Boolean hasRequiredLevel(Long userId, Integer requiredLevel) {
        if (requiredLevel == 0) return true;
        User user = findUser(userId);
        return user != null && user.getLevel() >= requiredLevel;
    }

    public Boolean userIsExist(Long id) {
        return userRepository.existsById(id);
    }

    public Boolean userIsExist(String username) {
        return userRepository.existsByUsername(username);
    }

    public void flush() {
        userRepository.flush();
    }

    public void update(User user) {
        processAndSendUser(user);
    }

    public void saveAll(List<User> users) {
        userRepository.saveAll(users);
    }

    public void saveAllAndFlush(List<User> users) {
        userRepository.saveAllAndFlush(users);
    }

    public void delete(User user) {
        userRepository.delete(user);
        userRedisTemplate.delete(CACHE_KEY + user.getId());
    }

    private void processAndSendUser(User user) {
        userProducer.sendUser(user);
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

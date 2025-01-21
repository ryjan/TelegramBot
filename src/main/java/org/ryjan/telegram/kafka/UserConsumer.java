package org.ryjan.telegram.kafka;

import org.ryjan.telegram.config.RedisConfig;
import org.ryjan.telegram.interfaces.repos.jpa.UserRepository;
import org.ryjan.telegram.model.users.User;
import org.ryjan.telegram.services.ServiceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.ryjan.telegram.kafka.UserProducer.USER_FIND_TOPIC;

@Service
public class UserConsumer extends ServiceBuilder {
    private final Map<Long, CompletableFuture<Void>> futureMap = new ConcurrentHashMap<>();

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RedisTemplate<String, User> userRedisTemplate;

    @KafkaListener(topics = "user-xp-topic", groupId = "user-xp-topic", containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consumeUserXp(List<User> users) {
        userRepository.saveAll(users);
        //userService.flush();
    }

    @KafkaListener(topics = USER_FIND_TOPIC, groupId = USER_FIND_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void consumeFindUserRequest(List<Long> userIds) {
        List<User> users = userRepository.findAllById(userIds);

        userRedisTemplate.opsForValue().multiSet(users.stream()
                .collect(Collectors.toMap(
                        user -> RedisConfig.USER_CACHE_KEY + user.getId(),
                        user -> user)
                ));

        for (Long userId : userIds) {
            CompletableFuture<Void> future = futureMap.remove(userId);
            if (future != null) {
                future.complete(null);
            }
        }
    }

    public CompletableFuture<Void> registerFuture(Long userId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        futureMap.put(userId, future);
        return future;
    }
}

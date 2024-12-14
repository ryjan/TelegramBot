package org.ryjan.telegram.kafka;

import org.ryjan.telegram.config.KafkaConfig;
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
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.ryjan.telegram.kafka.UserProducer.FIND_USER_TOPIC;
import static org.ryjan.telegram.kafka.UserProducer.USER_CACHE_TOPIC;

@Service
public class UserConsumer extends ServiceBuilder {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProducer userProducer;
    @Autowired
    private RedisTemplate<String, User> userRedisTemplate;

    @KafkaListener(topics = "user-xp-topic", groupId = "user-xp-topic", containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consumeUserXp(List<User> users) {
        userService.saveAll(users);
        //userService.flush();
    }

    @KafkaListener(topics = FIND_USER_TOPIC, groupId = "find-user", containerFactory = "kafkaListenerContainerFactory")
    public void consumeFindUserRequest(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        User user;

        if (userOptional.isPresent()) {
            user = userOptional.get();
            userProducer.sendUserCache(user);
        }
    }

    @KafkaListener(topics = USER_CACHE_TOPIC, groupId = "user-cache-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeUserCache(User user) {
        userRedisTemplate.opsForValue().set(RedisConfig.USER_CACHE_KEY + user.getId(), user, 10, TimeUnit.MINUTES);
    }
}

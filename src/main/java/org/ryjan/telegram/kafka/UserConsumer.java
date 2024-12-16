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
import java.util.stream.Collectors;

import static org.ryjan.telegram.kafka.UserProducer.FIND_USER_TOPIC;

@Service
public class UserConsumer extends ServiceBuilder {
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

    @KafkaListener(topics = FIND_USER_TOPIC, groupId = FIND_USER_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void consumeFindUserRequest(List<Long> userId) {
        List<User> users = userRepository.findAllById(userId);

        userRedisTemplate.opsForValue().multiSet(users.stream()
                .collect(Collectors.toMap(
                        user -> RedisConfig.USER_CACHE_KEY + user.getId(),
                        user -> user)
                ));
    }
}

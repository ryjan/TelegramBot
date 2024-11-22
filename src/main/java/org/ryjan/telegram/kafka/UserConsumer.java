package org.ryjan.telegram.kafka;

import org.ryjan.telegram.model.users.User;
import org.ryjan.telegram.services.ServiceBuilder;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserConsumer extends ServiceBuilder {

    @KafkaListener(topics = "user-xp-topic", groupId = "user-xp-topic", containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consumeUserXp(List<User> users) {
        userService.saveAll(users);
    }
}

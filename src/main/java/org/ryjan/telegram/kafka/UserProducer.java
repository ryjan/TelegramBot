package org.ryjan.telegram.kafka;

import org.ryjan.telegram.config.KafkaConfig;
import org.ryjan.telegram.model.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserProducer {
    public static final String FIND_USER_TOPIC = "find-user-topic";
    public static final String USER_RESPONSE_TOPIC = "user-response-topic";
    public static final String USER_CACHE_TOPIC = "user-cache-topic";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUser(User user) {
        kafkaTemplate.send("user-xp-topic", user);
    }

    public void findUser(Long userId) {
        kafkaTemplate.send(FIND_USER_TOPIC, userId);
    }

    public void sendUserResponse(User user) {
        kafkaTemplate.send(USER_RESPONSE_TOPIC, user);
    }

    public void sendUserCache(User user) {
        kafkaTemplate.send(USER_CACHE_TOPIC, user);
    }
}

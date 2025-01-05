package org.ryjan.telegram.kafka;

import org.ryjan.telegram.model.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class UserProducer {
    public static final String FIND_USER_TOPIC = "user-find-topic";
    public static final String USER_RESPONSE_TOPIC = "user-response-topic";
    public static final String USER_CACHE_TOPIC = "user-cache-topic";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private UserConsumer userConsumer;

    public void sendUser(User user) {
        kafkaTemplate.send("user-xp-topic", user);
    }

    public CompletableFuture<Void> findUser(Long userId) {
        CompletableFuture<Void> future = userConsumer.registerFuture(userId);
        kafkaTemplate.send(FIND_USER_TOPIC, userId);
        return future;
    }

    public void sendUserResponse(User user) {
        kafkaTemplate.send(USER_RESPONSE_TOPIC, user);
    }

    public void sendUserCache(User user) {
        kafkaTemplate.send(USER_CACHE_TOPIC, user);
    }
}

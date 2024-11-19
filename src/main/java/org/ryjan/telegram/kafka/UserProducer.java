package org.ryjan.telegram.kafka;

import org.ryjan.telegram.model.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserProducer {

    @Autowired
    private KafkaTemplate<String, User> kafkaTemplate;

    public void sendUser(User user) {
        kafkaTemplate.send("user-xp-topic", user);
    }
}

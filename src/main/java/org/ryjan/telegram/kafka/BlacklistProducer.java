package org.ryjan.telegram.kafka;

import org.ryjan.telegram.model.groups.Blacklist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BlacklistProducer {
    public static final String FIND_BLACKLIST_TOPIC = "find-blacklist-topic";
    public static final String SEND_BLACKLIST_TOPIC = "send-blacklist-topic";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendBlacklist(Blacklist blacklist) {
        kafkaTemplate.send(SEND_BLACKLIST_TOPIC, blacklist);
    }

    public void findBlacklist(Long blacklistId) {
        kafkaTemplate.send(FIND_BLACKLIST_TOPIC, blacklistId);
    }
}

package org.ryjan.telegram.kafka;

import org.ryjan.telegram.model.groups.Groups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class GroupsProducer {
    public static final String GROUP_SAVE_TOPIC = "group-save-topic";
    public static final String GROUP_DELETE_TOPIC = "group-delete-topic";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendGroup(Groups group) {
        kafkaTemplate.send(GROUP_SAVE_TOPIC, group);
    }

    public void sendToDelete(Groups group) {
        kafkaTemplate.send(GROUP_DELETE_TOPIC, group);
    }
}

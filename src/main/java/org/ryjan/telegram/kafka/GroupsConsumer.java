package org.ryjan.telegram.kafka;

import org.ryjan.telegram.interfaces.repos.jpa.GroupsRepository;
import org.ryjan.telegram.model.groups.Groups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.ryjan.telegram.kafka.GroupsProducer.GROUP_DELETE_TOPIC;
import static org.ryjan.telegram.kafka.GroupsProducer.GROUP_SAVE_TOPIC;

@Service
public class GroupsConsumer {

    @Autowired
    private GroupsRepository groupsRepository;

    @KafkaListener(topics = GROUP_SAVE_TOPIC, groupId = GROUP_SAVE_TOPIC,
            containerFactory = "kafkaListenerContainerFactory")
    public void consumeGroups(List<Groups> groups) {
        groupsRepository.saveAll(groups);
    }

    @KafkaListener(topics = GROUP_DELETE_TOPIC, groupId = GROUP_DELETE_TOPIC,
            containerFactory = "kafkaListenerContainerFactory")
    public void consumeToDeleteGroups(List<Groups> groups) {
        groupsRepository.deleteAll(groups);
    }
}

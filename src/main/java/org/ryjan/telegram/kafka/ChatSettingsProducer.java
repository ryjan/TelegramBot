package org.ryjan.telegram.kafka;

import org.ryjan.telegram.model.groups.ChatSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatSettingsProducer {
    public static final String FIND_CHAT_SETTINGS_TOPIC = "find-chat-settings-topic";
    public static final String FIND_CHAT_SETTINGS_BLACKLIST_TOPIC = "find-chat-settings-blacklist-topic";
    public static final String FIND_CHAT_SETTINGS_LEVELS_TOPIC = "find-chat-settings-levels-topic";
    public static final String SEND_CHAT_SETTINGS_TOPIC = "send-chat-settings-topic";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendChatSettings(ChatSettings chatSettings) {
        kafkaTemplate.send(SEND_CHAT_SETTINGS_TOPIC, chatSettings);
    }

    public void findChatSettings(Long groupId) {
        kafkaTemplate.send(FIND_CHAT_SETTINGS_TOPIC, groupId);
    }

    public void findChatSettingsBlacklist(Long groupId) {
        kafkaTemplate.send(FIND_CHAT_SETTINGS_BLACKLIST_TOPIC, groupId);
    }

    public void findChatSettingsLevels(Long groupId) {
        kafkaTemplate.send(FIND_CHAT_SETTINGS_LEVELS_TOPIC, groupId);
    }
}

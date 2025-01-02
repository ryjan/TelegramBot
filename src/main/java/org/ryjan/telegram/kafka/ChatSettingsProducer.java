package org.ryjan.telegram.kafka;

import org.ryjan.telegram.commands.groups.utils.GroupChatSettings;
import org.ryjan.telegram.model.groups.ChatSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ChatSettingsProducer {
    public static final String FIND_CHAT_SETTINGS_TOPIC = "find-chat-settings-topic";
    public static final String FIND_CHAT_SETTINGS_BLACKLIST_TOPIC = "find-chat-settings-blacklist-topic";
    public static final String FIND_CHAT_SETTINGS_LEVELS_TOPIC = "find-chat-settings-levels-topic";
    public static final String SEND_CHAT_SETTINGS_TOPIC = "send-chat-settings-topic";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private ChatSettingsConsumer chatSettingsConsumer;

    public void sendChatSettings(ChatSettings chatSettings) {
        kafkaTemplate.send(SEND_CHAT_SETTINGS_TOPIC, chatSettings);
    }

    public void findChatSettings(Long groupId) {
        kafkaTemplate.send(FIND_CHAT_SETTINGS_TOPIC, groupId);
    }

    public CompletableFuture<Void> findChatSetting(Long groupId, GroupChatSettings settingsType) {
        CompletableFuture<Void> future = chatSettingsConsumer.registerFuture(groupId);

        switch (settingsType) {
            case BLACKLIST:
                kafkaTemplate.send(FIND_CHAT_SETTINGS_BLACKLIST_TOPIC, groupId);
                break;
            case LEVELS:
                kafkaTemplate.send(FIND_CHAT_SETTINGS_LEVELS_TOPIC, groupId);
                break;
        }

        return future;
    }

    public void findChatSettingsLevels(Long groupId) {
        kafkaTemplate.send(FIND_CHAT_SETTINGS_LEVELS_TOPIC, groupId);
    }
}

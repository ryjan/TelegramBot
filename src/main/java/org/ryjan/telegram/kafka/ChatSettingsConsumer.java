package org.ryjan.telegram.kafka;

import org.ryjan.telegram.commands.groups.utils.GroupChatSettings;
import org.ryjan.telegram.config.RedisConfig;
import org.ryjan.telegram.interfaces.repos.jpa.BlacklistRepository;
import org.ryjan.telegram.interfaces.repos.jpa.ChatSettingsRepository;
import org.ryjan.telegram.model.groups.Blacklist;
import org.ryjan.telegram.model.groups.ChatSettings;
import org.ryjan.telegram.services.ServiceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.ryjan.telegram.kafka.ChatSettingsProducer.*;


@Service
public class ChatSettingsConsumer extends ServiceBuilder {
    private final Map<Long, CompletableFuture<Void>> futureMap = new ConcurrentHashMap<>();

    @Autowired
    private ChatSettingsRepository chatSettingsRepository;
    @Autowired
    private RedisTemplate<String, ChatSettings> chatSettingsRedisTemplate;

    @KafkaListener(topics = SEND_CHAT_SETTINGS_TOPIC, groupId = SEND_CHAT_SETTINGS_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consumeChatSettings(List<ChatSettings> chatSettings) {
        chatSettingsRepository.saveAll(chatSettings);
    }

    @KafkaListener(topics = SEND_TO_DELETE_CHAT_SETTINGS_TOPIC, groupId = SEND_TO_DELETE_CHAT_SETTINGS_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consumeToDeleteChatSettings(List<ChatSettings> chatSettings) {
        chatSettingsRepository.deleteAll(chatSettings);
    }

    @KafkaListener(topics = FIND_CHAT_SETTINGS_TOPIC, groupId = FIND_CHAT_SETTINGS_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void consumeFindChatSettingsRequest(List<Long> groupId) {
        List<ChatSettings> chatSettings = chatSettingsRepository.findAllByGroupIdIn(groupId);

        chatSettings.forEach(chatSetting ->
                chatSettingsRedisTemplate.opsForHash().put(
                        RedisConfig.CHAT_SETTINGS_CACHE_KEY,
                        RedisConfig.CHAT_SETTINGS_CACHE_KEY + chatSetting.getSettingKey() + chatSetting.getGroupId(),
                        chatSetting
                        )
        ); // слишком громостко, не подходит.
    }

    @KafkaListener(topics = {
            FIND_CHAT_SETTINGS_BLACKLIST_TOPIC,
            FIND_CHAT_SETTINGS_LEVELS_TOPIC,
            FIND_CHAT_SETTINGS_BLACKLIST_NOTIFICATIONS_TOPIC,
            FIND_CHAT_SETTINGS_SILENCE_MODE_TOPIC,
            FIND_CHAT_SETTINGS_SILENCE_MODE_END_TIME_TOPIC
    },
            groupId = "chat-settings-consumer-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeFindChatSettingsBlacklistRequest(List<Long> groupIds, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        GroupChatSettings settingType = getSettingsTypeFromTopic(topic);
        List<ChatSettings> chatSettings = chatSettingsRepository.findByGroupIdInAndSettingKey(groupIds, settingType.getDisplayname());

        chatSettingsRedisTemplate.opsForValue().multiSet(chatSettings.stream()
                .collect(Collectors.toMap(
                        chatSetting -> RedisConfig.CHAT_SETTINGS_CACHE_KEY + settingType.getDisplayname() + chatSetting.getGroupId(),
                        chatSetting -> chatSetting)
                ));

        for (Long groupId : groupIds) {
            CompletableFuture<Void> future = futureMap.remove(groupId);
            if (future != null) {
                future.complete(null);
            }
        }
    }

    public CompletableFuture<Void> registerFuture(Long groupId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        futureMap.put(groupId, future);
        return future;
    }

    private GroupChatSettings getSettingsTypeFromTopic(String topic) {
        return switch (topic) {
            case FIND_CHAT_SETTINGS_BLACKLIST_TOPIC -> GroupChatSettings.BLACKLIST;
            case FIND_CHAT_SETTINGS_BLACKLIST_NOTIFICATIONS_TOPIC -> GroupChatSettings.BLACKLIST_NOTIFICATIONS;
            case FIND_CHAT_SETTINGS_LEVELS_TOPIC -> GroupChatSettings.LEVELS;
            case FIND_CHAT_SETTINGS_SILENCE_MODE_TOPIC -> GroupChatSettings.SILENCE_MODE;
            default -> throw new IllegalArgumentException("Unknown topic: " + topic);
        };
    }
}

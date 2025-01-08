package org.ryjan.telegram.services;

import org.ryjan.telegram.commands.groups.utils.GroupChatSettings;
import org.ryjan.telegram.commands.groups.utils.GroupSwitch;
import org.ryjan.telegram.config.RedisConfig;
import org.ryjan.telegram.kafka.ChatSettingsProducer;
import org.ryjan.telegram.model.groups.ChatSettings;
import org.ryjan.telegram.model.groups.Groups;
import org.ryjan.telegram.interfaces.repos.jpa.ChatSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class ChatSettingsService extends ServiceBuilder {
    @Autowired
    private ChatSettingsRepository chatSettingsRepository;
    @Autowired
    private ChatSettingsProducer chatSettingsProducer;
    @Autowired
    private RedisTemplate<String, ChatSettings> chatSettingsRedisTemplate;

    public ChatSettings addChatSettings(Long groupId, GroupChatSettings groupChatSettings, GroupSwitch groupSwitch) {
        Groups group = groupService.findGroup(groupId);
        ChatSettings chatSettings = findChatSettings(groupId, groupChatSettings);
        if (chatSettings == null) {
            chatSettings = new ChatSettings(group, groupChatSettings, groupSwitch);
            group.addChatSetting(chatSettings);
            groupService.update(group);
        } else {
            chatSettings.setSettingValue(groupSwitch);
            update(chatSettings);
        }
        return chatSettings;
    }

    public ChatSettings addChatSettings(Groups group, GroupChatSettings groupChatSettings, GroupSwitch groupSwitch) {
        ChatSettings chatSettings = findChatSettings(group.getId(), groupChatSettings);
        if (chatSettings == null) {
            chatSettings = new ChatSettings(group, groupChatSettings, groupSwitch);
            group.addChatSetting(chatSettings);
            groupService.update(group);
        } else {
            chatSettings.setSettingValue(groupSwitch);
            update(chatSettings);
        }
        return chatSettings;
    }

    public void addChatSettings(Long groupId, GroupChatSettings groupChatSettings, String settingValue) {
        Groups group = groupService.findGroup(groupId);
        ChatSettings chatSettings = findChatSettings(groupId, groupChatSettings);
        if (chatSettings == null) {
            chatSettings = new ChatSettings(group, groupChatSettings, settingValue);
            group.addChatSetting(chatSettings);
            groupService.update(group);
        } else {
            chatSettings.setSettingValue(settingValue);
            update(chatSettings);
        }
    }

    public boolean isBlacklistEnabled(Long groupId) {
        return findChatSetting(groupId, GroupChatSettings.BLACKLIST).getSettingValue().equals(GroupSwitch.ON.getDisplayname());
    }

    public void replaceSettingValue(Long groupId, GroupChatSettings settingKey, String settingValue) {
        ChatSettings chatSettings = chatSettingsService.findChatSettings(groupId, settingKey);
        chatSettings.setSettingValue(settingValue);
        chatSettingsRedisTemplate.opsForValue().set(RedisConfig.CHAT_SETTINGS_CACHE_KEY
                + settingKey.getDisplayname() + groupId, chatSettings);
        chatSettingsService.update(chatSettings);
    }

    public ChatSettings findChatSettings(Long groupId, GroupChatSettings groupChatSettings) {
        return chatSettingsRepository.findByGroupIdAndSettingKey(groupId, groupChatSettings.getDisplayname());
    }

    public ChatSettings findBlacklistSettings(Long groupId) {
        return findChatSetting(groupId, GroupChatSettings.BLACKLIST);
    }

    public ChatSettings findBlacklistNotifications(Long groupId) {
        return findChatSetting(groupId, GroupChatSettings.BLACKLIST_NOTIFICATIONS);
    }

    public ChatSettings findLevelsSettings(Long groupId) {
        return findChatSetting(groupId, GroupChatSettings.LEVELS);
    }

    public ChatSettings findSilenceMode(Long groupId) {
        return findChatSetting(groupId, GroupChatSettings.SILENCE_MODE);
    }

    public ChatSettings findSilenceModeEndTime(Long groupId) {
        return findChatSetting(groupId, GroupChatSettings.SILENCE_MODE_END_TIME);
    }

    private ChatSettings findChatSetting(Long groupId, GroupChatSettings settingsType) {
        String redisKey = RedisConfig.CHAT_SETTINGS_CACHE_KEY
                + settingsType.getDisplayname()
                + groupId;
        ChatSettings chatSettings = chatSettingsRedisTemplate.opsForValue().get(redisKey);
        if (chatSettings == null) {
            CompletableFuture<Void> future = chatSettingsProducer.findChatSetting(groupId, settingsType);

            try {
                future.get(5, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new RuntimeException("Failed to fetch chat settings from Kafka", e);
            }

            chatSettings = chatSettingsRedisTemplate.opsForValue().get(redisKey);
        }

        if (chatSettings == null) {
            chatSettings = addChatSettings(groupId, settingsType, GroupSwitch.OFF);
        }
        return chatSettings;
    }

    public void update(ChatSettings chatSettings) {
        chatSettingsProducer.sendChatSettings(chatSettings);
    }

    public void delete(ChatSettings chatSettings) {
        chatSettingsProducer.sendToDeleteChatSettings(chatSettings);
    }
}

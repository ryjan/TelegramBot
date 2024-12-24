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
        System.out.println("isBlacklistEnabled " + findBlacklistSettings(groupId).getSettingValue());
        System.out.println("isBlacklistEnabled " + findBlacklistSettings(groupId).getSettingValue());
        System.out.println("isBlacklistEnabled " + findBlacklistSettings(groupId).getSettingValue());
        System.out.println("isBlacklistEnabled " + findBlacklistSettings(groupId).getSettingValue());
        return findBlacklistSettings(groupId).getSettingValue().equals(GroupSwitch.ON.getDisplayname());
    }

    public void replaceBlacklistValue(Long groupId, String settingsKey, String settingsValue) {
        ChatSettings chatSettings = chatSettingsService.findChatSettings(groupId, settingsKey);
        chatSettings.setSettingValue(settingsValue);
        chatSettingsRedisTemplate.opsForValue().set(RedisConfig.CHAT_SETTINGS_CACHE_KEY
                + GroupChatSettings.BLACKLIST.getDisplayname() + groupId, chatSettings);
        chatSettingsService.update(chatSettings);
    }

    public ChatSettings findChatSettings(Long groupId, GroupChatSettings groupChatSettings) {
        return chatSettingsRepository.findByGroupIdAndSettingKey(groupId, groupChatSettings.getDisplayname());
    }

    public ChatSettings findChatSettings(Long groupId, String groupChatSettings) {
        return chatSettingsRepository.findByGroupIdAndSettingKey(groupId, groupChatSettings);
    }

    public ChatSettings findBlacklistSettings(Long groupId) {
        ChatSettings chatSettings = chatSettingsRedisTemplate.opsForValue().get(RedisConfig.CHAT_SETTINGS_CACHE_KEY
                + GroupChatSettings.BLACKLIST.getDisplayname() + groupId);
        if (chatSettings == null) {
            chatSettingsProducer.findChatSettingsBlacklist(groupId);
            chatSettings = chatSettingsRedisTemplate.opsForValue().get(RedisConfig.CHAT_SETTINGS_CACHE_KEY
                    + GroupChatSettings.BLACKLIST.getDisplayname() + groupId);
        }
        return chatSettings;
    }

    public ChatSettings chatSettingsCheckKeyValue(Long groupId, String key, String value) {
        return chatSettingsRepository.findByGroupIdAndSettingKeyAndSettingValue(groupId, key, value);
    }

    public void update(ChatSettings chatSettings) {
        chatSettingsRepository.save(chatSettings);
    }

    public void delete(ChatSettings chatSettings) {
        chatSettingsRepository.delete(chatSettings);
    }
}

package org.ryjan.telegram.services;

import org.ryjan.telegram.model.groups.ChatSettings;
import org.ryjan.telegram.model.groups.Groups;
import org.ryjan.telegram.interfaces.repos.jpa.ChatSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatSettingsService extends ServiceBuilder {

    @Autowired
    private ChatSettingsRepository chatSettingsRepository;

    public void addChatSettings(Long groupId, String settingsKey, String settingsValue) {
        Groups group = groupService.findGroup(groupId);
        ChatSettings chatSettings = findChatSettings(groupId, settingsKey);
        if (chatSettings == null) {
            chatSettings = new ChatSettings(settingsKey, settingsValue, group);
            group.addChatSetting(chatSettings);
            groupService.update(group);
        } else {
            chatSettings.setSettingValue(settingsValue);
            update(chatSettings);
        }
    }

    public void addChatSettings(Groups group, String settingsKey, String settingsValue) {
        ChatSettings chatSettings = findChatSettings(group.getId(), settingsKey);
        if (chatSettings == null) {
            chatSettings = new ChatSettings(settingsKey, settingsValue, group);
            group.addChatSetting(chatSettings);
            groupService.update(group);
        } else {
            chatSettings.setSettingValue(settingsValue);
            update(chatSettings);
        }
    }

    public ChatSettings findChatSettings(Long groupId, String settingsKey) {
        return chatSettingsRepository.findByGroupIdAndSettingKey(groupId, settingsKey);
    }

    public ChatSettings chatSettingsCheckKeyValue(long groupId, String key, String value) {
        return chatSettingsRepository.findByGroupIdAndSettingKeyAndSettingValue(groupId, key, value);
    }

    public void update(ChatSettings chatSettings) {
        chatSettingsRepository.save(chatSettings);
    }

    public void delete(ChatSettings chatSettings) {
        chatSettingsRepository.delete(chatSettings);
    }
}

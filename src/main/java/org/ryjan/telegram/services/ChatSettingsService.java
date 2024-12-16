package org.ryjan.telegram.services;

import org.ryjan.telegram.commands.groups.utils.GroupChatSettings;
import org.ryjan.telegram.commands.groups.utils.GroupSwitch;
import org.ryjan.telegram.model.groups.ChatSettings;
import org.ryjan.telegram.model.groups.Groups;
import org.ryjan.telegram.interfaces.repos.jpa.ChatSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatSettingsService extends ServiceBuilder {

    @Autowired
    private ChatSettingsRepository chatSettingsRepository;

    public void addChatSettings(Long groupId, GroupChatSettings groupChatSettings, GroupSwitch groupSwitch) {
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
    }

    public void addChatSettings(Groups group, GroupChatSettings groupChatSettings, GroupSwitch groupSwitch) {
        ChatSettings chatSettings = findChatSettings(group.getId(), groupChatSettings);
        if (chatSettings == null) {
            chatSettings = new ChatSettings(group, groupChatSettings, groupSwitch);
            group.addChatSetting(chatSettings);
            groupService.update(group);
        } else {
            chatSettings.setSettingValue(groupSwitch);
            update(chatSettings);
        }
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
        return chatSettingsCheckKeyValue(groupId, GroupChatSettings.BLACKLIST.getDisplayname(), GroupSwitch.ON.getDisplayname()) != null;
    }

    public ChatSettings findChatSettings(Long groupId, GroupChatSettings groupChatSettings) {
        return chatSettingsRepository.findByGroupIdAndSettingKey(groupId, groupChatSettings.getDisplayname());
    }

    public ChatSettings findChatSettings(Long groupId, String groupChatSettings) {
        return chatSettingsRepository.findByGroupIdAndSettingKey(groupId, groupChatSettings);
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

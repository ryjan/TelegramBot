package org.ryjan.telegram.services;

import jakarta.persistence.EntityManager;
import org.hibernate.Hibernate;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.groups.Blacklist;
import org.ryjan.telegram.model.groups.ChatSettings;
import org.ryjan.telegram.model.groups.Groups;
import org.ryjan.telegram.repos.jpa.BlacklistRepository;
import org.ryjan.telegram.repos.jpa.ChatSettingsRepository;
import org.ryjan.telegram.repos.jpa.GroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GroupService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    GroupsRepository groupsRepository;

    @Autowired
    ChatSettingsRepository chatSettingsRepository;

    @Autowired
    BlacklistRepository blacklistRepository;

    @Autowired
    @Lazy
    BotMain botMain;

    public void addToBlacklist(Groups group, Blacklist blacklist) {
        List<Blacklist> list = group.getBlacklists();
        list.add(blacklist);
        group.setBlacklists(list);
        blacklist.setGroup(group);
        groupsRepository.save(group);
    }

    @Transactional
    public void addToBlacklist(Long groupId, Blacklist blacklist) {
        Groups group = findGroup(groupId);
        blacklist.setGroup(group);
        group.getBlacklists().add(blacklist);
        groupsRepository.save(group); // попробовать привязать BlacklistList группы и добавить туда blacklist
    }

    public void addChatSettings(Long groupId, String settingsKey, String settingsValue) {
        Groups group = findGroup(groupId);
        ChatSettings chatSettings = findChatSettings(groupId, settingsKey);
        if (chatSettings == null) {
            chatSettings = new ChatSettings(settingsKey, settingsValue, group);
            group.addChatSetting(chatSettings);
            update(group);
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
            update(group);
        } else {
            chatSettings.setSettingValue(settingsValue);
            update(chatSettings);
        }
    }

    public boolean silenceModeStatus(long groupId) {
        return chatSettingsCheckKeyValue(groupId, "silenceMode", "enabled") != null;
    }

    public boolean blacklistStatus(long groupId) {
        return chatSettingsCheckKeyValue(groupId, "blacklist", "enabled") != null;
    }

    public ChatSettings chatSettingsCheckKeyValue(long groupId, String key, String value) {
        return chatSettingsRepository.findByGroupIdAndSettingKeyAndSettingValue(groupId, key, value);
    }

    public void replaceBlacklistValue(long groupId, String settingsKey, String settingsValue) {
        ChatSettings chatSettings = findChatSettings(groupId, settingsKey);
        chatSettings.setSettingValue(settingsValue);
        update(chatSettings);
    }

    public Groups findGroup(Long id) {
        return groupsRepository.findById(id).orElse(null);
    }

    public Blacklist findBlacklist(long id) {
        return blacklistRepository.findByUserId(id);
    }

    public List<Blacklist> findAllBlacklists(long groupId) {
        return blacklistRepository.findByGroupId(groupId);
    }

    @Transactional
    public List<Blacklist> findAllBlacklistsWithInitializedGroups(long groupId) {
        List<Blacklist> blacklists = blacklistRepository.findByGroupId(groupId);
        blacklists.forEach(blacklist -> Hibernate.initialize(blacklist.getGroup().getChatSettings()));
        return blacklists;
        //DO NOT USE
    }

    public ChatSettings findChatSettings(Long groupId, String settingsKey) {
        return chatSettingsRepository.findByGroupIdAndSettingKey(groupId, settingsKey);
    }

    public boolean isExistGroup(String groupName) {
        return groupsRepository.existsByGroupName(groupName);
    }

    public boolean isExistGroup(Long groupId) {
        return groupsRepository.existsById(groupId);
    }

    public boolean isExistBlacklist(Long userId) {
        return blacklistRepository.existsByUserId(userId);
    }

    public boolean groupIsExist(Long id) {
        return groupsRepository.existsById(id);
    }

    public void update(Groups group) {
        groupsRepository.save(group);
    }

    public void update(Blacklist blacklist) {
        blacklistRepository.save(blacklist);
    }

    public void update(ChatSettings chatSettings) {
        chatSettingsRepository.save(chatSettings);
    }

    public void update(Groups group, ChatSettings chatSettings) {
        groupsRepository.save(group);
        chatSettingsRepository.save(chatSettings);
    }

    public void delete(Groups group) {
        groupsRepository.delete(group);
    }

    public void delete(Blacklist blacklist) {
        blacklistRepository.delete(blacklist);
    }

    public BotMain getBotMain() {
        return botMain;
    }
}

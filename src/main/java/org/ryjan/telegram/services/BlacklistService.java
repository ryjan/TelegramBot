package org.ryjan.telegram.services;

import org.ryjan.telegram.model.groups.Blacklist;
import org.ryjan.telegram.model.groups.ChatSettings;
import org.ryjan.telegram.model.groups.Groups;
import org.ryjan.telegram.repos.jpa.BlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BlacklistService extends ServiceBuilder {

    @Autowired
    BlacklistRepository blacklistRepository;

    public BlacklistService(MainServices mainServices) {
        this.groupService = mainServices.getGroupService();
        this.chatSettingsService = mainServices.getChatSettingsService();

    }

    @Transactional
    public void addToBlacklist(Long groupId, Blacklist blacklist) {
        Groups group = groupService.findGroup(groupId);
        blacklist.setGroup(group);
        group.getBlacklists().add(blacklist);
        groupService.update(group); // попробовать привязать BlacklistList группы и добавить туда blacklist
    }

    public void addToBlacklist(Groups group, Blacklist blacklist) {
        List<Blacklist> list = group.getBlacklists();
        list.add(blacklist);
        group.setBlacklists(list);
        blacklist.setGroup(group);
        groupService.update(group);
    }

    public boolean blacklistStatus(long groupId) {
        return chatSettingsService.chatSettingsCheckKeyValue(groupId, "blacklist", "enabled") != null;
    }

    public void replaceBlacklistValue(long groupId, String settingsKey, String settingsValue) {
        ChatSettings chatSettings = chatSettingsService.findChatSettings(groupId, settingsKey);
        chatSettings.setSettingValue(settingsValue);
        chatSettingsService.update(chatSettings);
    }

    public List<Blacklist> findAllBlacklists(long groupId) {
        return blacklistRepository.findByGroupId(groupId);
    }

    public Blacklist findBlacklist(long id) {
        return blacklistRepository.findByUserId(id);
    }

    public boolean isExistBlacklist(Long userId) {
        return blacklistRepository.existsByUserId(userId);
    }


    public void update(Blacklist blacklist) {
        blacklistRepository.save(blacklist);
    }

    public void delete(Blacklist blacklist) {
        blacklistRepository.delete(blacklist);
    }
}

package org.ryjan.telegram.services;

import org.ryjan.telegram.commands.groups.utils.GroupChatSettings;
import org.ryjan.telegram.commands.groups.utils.GroupSwitch;
import org.ryjan.telegram.config.RedisConfig;
import org.ryjan.telegram.kafka.BlacklistProducer;
import org.ryjan.telegram.model.groups.Blacklist;
import org.ryjan.telegram.model.groups.ChatSettings;
import org.ryjan.telegram.model.groups.Groups;
import org.ryjan.telegram.interfaces.repos.jpa.BlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BlacklistService extends ServiceBuilder {

    @Autowired
    private BlacklistRepository blacklistRepository;
    @Autowired
    private BlacklistProducer blacklistProducer;
    @Autowired
    private RedisTemplate<String, Blacklist> blacklistRedisTemplate;

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

    public boolean isBlacklistEnabled(Long groupId) {
        return chatSettingsService.isBlacklistEnabled(groupId);
    }

    public void replaceBlacklistValue(Long groupId, String settingsKey, String settingsValue) {
        ChatSettings chatSettings = chatSettingsService.findChatSettings(groupId, settingsKey);
        chatSettings.setSettingValue(settingsValue);
        chatSettingsService.update(chatSettings);
    }

    public List<Blacklist> findAllBlacklistsById(Long groupId) {
        return blacklistRepository.findByGroupId(groupId);
    }

    public Blacklist findBlacklist(Long id) {
        Blacklist blacklist = blacklistRedisTemplate.opsForValue().get(RedisConfig.BLACKLIST_CACHE_KEY + id);

        if (blacklist == null) {
            return blacklistRepository.findByUserId(id);
        }
        return blacklist;
    }

    public Blacklist findBlacklist(String username, Long groupId) {
        return blacklistRepository.findByUsernameAndGroupId(username, groupId);
    }

    public boolean isExistBlacklist(Long userId) {
        return blacklistRepository.existsByUserId(userId);
    }


    public void update(Blacklist blacklist) {
        processAndSendBlacklist(blacklist);
    }

    public void delete(Blacklist blacklist) {
        blacklistRepository.delete(blacklist);
    }

    private void processAndSendBlacklist(Blacklist blacklist) {
        blacklistProducer.sendBlacklist(blacklist);
    }
}

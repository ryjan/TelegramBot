package org.ryjan.telegram.services;

import lombok.Getter;
import org.ryjan.telegram.commands.groups.GroupStatus;
import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.config.RedisConfig;
import org.ryjan.telegram.model.groups.ChatSettings;
import org.ryjan.telegram.model.groups.Groups;
import org.ryjan.telegram.interfaces.repos.jpa.GroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

import java.security.Permission;
import java.util.concurrent.TimeUnit;

@Service
public class GroupService extends ServiceBuilder {
    @Getter
    private final String CACHE_KEY = "group_status:";
    @Getter
    private final String OWNER_GROUP_STATE_CACHE_KEY = "owner_group_state:";
    private final String GROUP_CACHE_KEY = RedisConfig.GROUP_CACHE_KEY;

    @Autowired
    private GroupsRepository groupsRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisTemplate<String, Groups> groupsRedisTemplate;

    public GroupPermissions getPermissionFromChat(Long chatId, Long userId) {
        ChatMember chatMember = botService.getChatMember(chatId, userId);
        String status = chatMember.getStatus();

        return switch (status) {
            case "creator" -> GroupPermissions.CREATOR;
            case "administrator" -> GroupPermissions.ADMIN;
            default -> GroupPermissions.ANY;
        };
    }

    public boolean isGroupBanned(Long chatId) {
        String groupStatus = redisTemplate.opsForValue().get(CACHE_KEY + chatId);

        if (groupStatus == null) {
            Groups group = findGroup(chatId);
            if (group == null) return false;
            redisTemplate.opsForValue().set(CACHE_KEY + chatId, group.getStatus());
        }

        return GroupStatus.BANNED.getDisplayName().equals(groupStatus);
    }

    public Groups findGroup(Long id) {
        Groups group = groupsRedisTemplate.opsForValue().get(GROUP_CACHE_KEY + id);

        if (group == null) {
            group = groupsRepository.findById(id).orElse(null);
            assert group != null;
            groupsRedisTemplate.opsForValue().set(GROUP_CACHE_KEY + id, group, 10, TimeUnit.MINUTES);
        }
        return group;
    }

    public boolean isExistGroup(String groupName) {
        return groupsRepository.existsByGroupName(groupName);
    }

    public boolean isExistGroup(Long groupId) {
        return groupsRepository.existsById(groupId);
    }

    public boolean groupIsExist(Long id) {
        return groupsRepository.existsById(id);
    }

    public void update(Groups group) {
        groupsRepository.save(group);
    }

    public void update(Groups group, ChatSettings chatSettings) {
        update(group);
        chatSettingsService.update(chatSettings);
    }

    public void delete(Groups group) {
        groupsRepository.delete(group);
    }
}

package org.ryjan.telegram.services;

import org.ryjan.telegram.model.groups.ChatSettings;
import org.ryjan.telegram.model.groups.Groups;
import org.ryjan.telegram.repos.jpa.GroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class GroupService extends ServiceBuilder {

    @Autowired
    private GroupsRepository groupsRepository;

    public Groups findGroup(Long id) {
        return groupsRepository.findById(id).orElse(null);
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

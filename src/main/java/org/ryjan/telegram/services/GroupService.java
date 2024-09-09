package org.ryjan.telegram.services;

import jakarta.persistence.EntityManager;
import org.ryjan.telegram.model.groups.Blacklist;
import org.ryjan.telegram.model.groups.ChatSettings;
import org.ryjan.telegram.model.groups.Groups;
import org.ryjan.telegram.repos.BlacklistRepository;
import org.ryjan.telegram.repos.ChatSettingsRepository;
import org.ryjan.telegram.repos.GroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    GroupsRepository groupsRepository;

    @Autowired
    ChatSettingsRepository chatSettingsRepository;

    @Autowired
    BlacklistRepository blacklistRepository;

    @Transactional
    public void addToBlacklist(Groups group, Blacklist blacklist) {
        List<Blacklist> list = group.getBlacklists();
        list.add(blacklist);
        group.setBlacklists(list);
        blacklistRepository.save(blacklist);
        groupsRepository.save(group);
    }

    public Groups findGroup(Long id) {
        return groupsRepository.findById(id).orElse(null);
    }

    public boolean groupIsExist(String groupName) {
        return groupsRepository.existsByGroupName(groupName);
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

    public void update(Groups group, ChatSettings chatSettings) {
        groupsRepository.save(group);
        chatSettingsRepository.save(chatSettings);
    }

    public void delete(Groups group) {
        groupsRepository.delete(group);
    }
}

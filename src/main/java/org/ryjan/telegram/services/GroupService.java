package org.ryjan.telegram.services;

import jakarta.persistence.EntityManager;
import org.ryjan.telegram.model.groups.Groups;
import org.ryjan.telegram.repos.BlacklistRepository;
import org.ryjan.telegram.repos.ChatSettingsRepository;
import org.ryjan.telegram.repos.GroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public boolean groupIsExist(String groupName) {
        return groupsRepository.existsByGroupName(groupName);
    }

    public boolean groupIsExist(Long id) {
        return groupsRepository.existsById(id);
    }

    public void update(Groups group) {
        groupsRepository.save(group);
    }

    public void delete(Groups group) {
        groupsRepository.delete(group);
    }
}

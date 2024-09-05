package org.ryjan.telegram.services;

import org.ryjan.telegram.model.Groups;
import org.ryjan.telegram.model.UserDatabase;
import org.ryjan.telegram.repos.GroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {
    @Autowired
    GroupsRepository groupsRepository;

    public void update(Groups group) {
        groupsRepository.save(group);
    }

    public void delete(Groups group) {
        groupsRepository.delete(group);
    }
}

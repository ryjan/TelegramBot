package org.ryjan.telegram.interfaces.repos.jpa;

import org.ryjan.telegram.model.groups.Groups;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupsRepository extends JpaRepository<Groups, Long> {

    public boolean existsById(Long id);
    public boolean existsByGroupName(String groupName);
}

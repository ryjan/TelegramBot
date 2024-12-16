package org.ryjan.telegram.interfaces.repos.jpa;

import org.ryjan.telegram.model.groups.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
    Blacklist findByUserId(Long userId);
    Blacklist findByUsernameAndGroupId(String username, Long groupId);

    List<Blacklist> findByGroupId(Long groupId);
    List<Blacklist> findByUserIdIn(List<Long> userIds);

    Boolean existsByUserId(Long userId);
}
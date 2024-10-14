package org.ryjan.telegram.interfaces.repos.jpa;

import org.ryjan.telegram.model.groups.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
    Blacklist findByUserId(long userId);
    List<Blacklist> findByGroupId(long groupId);
    boolean existsByUserId(long userId);
}

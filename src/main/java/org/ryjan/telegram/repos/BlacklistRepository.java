package org.ryjan.telegram.repos;

import org.ryjan.telegram.model.groups.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
    Blacklist findByUserId(long userId);
}

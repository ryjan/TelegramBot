package org.ryjan.telegram.repos;

import org.ryjan.telegram.model.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
}

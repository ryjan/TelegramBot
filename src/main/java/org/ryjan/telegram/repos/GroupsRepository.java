package org.ryjan.telegram.repos;

import org.ryjan.telegram.model.Groups;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupsRepository extends JpaRepository<Groups, Long> {

}

package org.ryjan.telegram.interfaces.repos.jpa;

import org.ryjan.telegram.model.users.UserDatabase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserDatabaseRepository extends JpaRepository<UserDatabase, Long> {
    UserDatabase findByUserTag(String tag);

    boolean existsByUserTag(String username);
 //   boolean existsById(long id);



}

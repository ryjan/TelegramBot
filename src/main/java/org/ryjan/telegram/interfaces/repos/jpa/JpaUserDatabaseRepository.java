package org.ryjan.telegram.interfaces.repos.jpa;

import org.ryjan.telegram.model.users.UserDatabase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserDatabaseRepository extends JpaRepository<UserDatabase, Long> {
    UserDatabase findUserDatabaseById(Long userId);
    UserDatabase findByUsername(String tag);

    boolean existsByUsername(String username);
 // boolean existsById(long id);



}

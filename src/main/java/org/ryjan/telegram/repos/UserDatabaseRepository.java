package org.ryjan.telegram.repos;

import org.ryjan.telegram.model.users.UserDatabase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDatabaseRepository extends JpaRepository<UserDatabase, Long> {
    UserDatabase findByUserTag(String tag);

    boolean existsByUserTag(String username);
 //   boolean existsById(long id);



}

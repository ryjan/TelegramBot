package org.ryjan.telegram.repos;

import org.ryjan.telegram.model.UserDatabase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserDatabaseRepository extends JpaRepository<UserDatabase, Long> {
    UserDatabase findUserDatabaseByUserTag(String username);
    UserDatabase findByUserTag(String tag);

    boolean existsByUserTag(String username);
 //   boolean existsById(long id);



}

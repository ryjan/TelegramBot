package org.ryjan.telegram.interfaces.repos.jpa;

import org.ryjan.telegram.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String tag);

    boolean existsByUsername(String username);
 // boolean existsById(long id);



}

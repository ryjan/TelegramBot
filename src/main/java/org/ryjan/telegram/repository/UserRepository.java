package org.ryjan.telegram.repository;

import jakarta.persistence.Column;
import org.ryjan.telegram.database.UserDatabase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDatabase, Long> {

    @Query("SELECT u From UserDatabase u WHERE u.userTag = :username")
    UserDatabase findByUsername(String username);
}

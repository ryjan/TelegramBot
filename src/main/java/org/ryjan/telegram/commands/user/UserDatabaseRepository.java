package org.ryjan.telegram.commands.user;

import org.ryjan.telegram.database.BankDatabase;
import org.ryjan.telegram.database.UserDatabase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDatabaseRepository extends JpaRepository<UserDatabase, Long> {
    UserDatabase findUserDatabaseById(long id);
    UserDatabase findUserDatabaseByUserTag(String username);

    BankDatabase findBankDatabaseById(long id);

    boolean existsUserDatabaseByUserTag(String username);
    boolean existsUserDatabaseById(long id);



}

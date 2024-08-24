package org.ryjan.telegram.repository;

import org.ryjan.telegram.commands.TransferLimit;
import org.ryjan.telegram.database.UserDatabase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferLimitRepository extends JpaRepository<TransferLimit, Long> {
    TransferLimit findByUser(UserDatabase userDatabase);
}

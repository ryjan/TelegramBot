package org.ryjan.telegram.repos.jpa;

import org.ryjan.telegram.commands.users.user.transfers.TransferLimit;
import org.ryjan.telegram.model.users.UserDatabase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferLimitRepository extends JpaRepository<TransferLimit, Long> {
    TransferLimit findByUserDatabase(UserDatabase userDatabase);
}

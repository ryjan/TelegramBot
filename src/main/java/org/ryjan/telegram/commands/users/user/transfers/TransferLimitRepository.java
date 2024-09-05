package org.ryjan.telegram.commands.users.user.transfers;

import org.ryjan.telegram.model.UserDatabase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferLimitRepository extends JpaRepository<TransferLimit, Long> {
    TransferLimit findByUserDatabase(UserDatabase userDatabase);
}

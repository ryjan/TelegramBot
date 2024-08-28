package org.ryjan.telegram.commands.user.transfers;

import org.ryjan.telegram.domain.UserDatabase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferLimitRepository extends JpaRepository<TransferLimit, Long> {
    TransferLimit findByUserDatabase(UserDatabase userDatabase);
}

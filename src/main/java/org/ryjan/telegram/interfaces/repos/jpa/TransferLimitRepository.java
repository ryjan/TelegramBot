package org.ryjan.telegram.interfaces.repos.jpa;

import org.ryjan.telegram.commands.users.user.transfers.TransferLimit;
import org.ryjan.telegram.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferLimitRepository extends JpaRepository<TransferLimit, Long> {
    TransferLimit findByUser(User user);
}

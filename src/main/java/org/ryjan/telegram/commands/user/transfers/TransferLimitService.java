package org.ryjan.telegram.commands.user.transfers;

import org.ryjan.telegram.config.BotConfig;
import org.ryjan.telegram.model.UserDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class TransferLimitService {
    @Autowired
    private TransferLimitRepository transferLimitRepository;

    public static final BigDecimal DAILY_LIMIT = BotConfig.DAILY_LIMIT;

    public boolean canTransfer(UserDatabase userDatabase, BigDecimal amount) {
        TransferLimit limit = getOrCreateLimit(userDatabase);

        if (!limit.getLastTransferDate().equals(LocalDate.now())) {
            limit.setDailyTransferAmount(BigDecimal.ZERO);
            limit.setLastTransferDate(LocalDate.now());
        }

        BigDecimal newTotal = limit.getDailyTransferAmount().add(amount);

        return newTotal.compareTo(DAILY_LIMIT) <= 0;
    }

    public void recordTransfer(UserDatabase userDatabase, BigDecimal amount) {
        TransferLimit limit = getOrCreateLimit(userDatabase);

        if (!limit.getLastTransferDate().equals(LocalDate.now())) {
            limit.setDailyTransferAmount(amount);
            limit.setLastTransferDate(LocalDate.now());
        } else {
            limit.setDailyTransferAmount(limit.getDailyTransferAmount().add(amount));
        }
    }

    private TransferLimit getOrCreateLimit(UserDatabase userDatabase) {
        TransferLimit limit = transferLimitRepository.findByUserDatabase(userDatabase);
        if (limit == null) {
            limit = new TransferLimit();
            limit.setUserDatabase(userDatabase);
            limit.setDailyTransferAmount(BigDecimal.ZERO);
            limit.setLastTransferDate(LocalDate.now());
        }
        return limit;
    }
}

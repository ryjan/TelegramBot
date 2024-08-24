package org.ryjan.telegram.services;

import org.ryjan.telegram.database.BankDatabase;
import org.ryjan.telegram.database.UserDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransferService {
    @Autowired
    private UserService userService;
    @Autowired
    private TransferLimitService transferLimitService;

    public boolean transferCoins(String fromUsername, String toUsername, BigDecimal amount) {
        UserDatabase fromUser = userService.findUser(fromUsername);
        UserDatabase toUser = userService.findUser(toUsername);

        if (fromUser == null || toUser == null) {
            return false;
        }

        if (!transferLimitService.canTransfer(fromUser, amount)) {
            return false;
        }

        BankDatabase fromBank = fromUser.getBank();
        BankDatabase tomBank = toUser.getBank();

        if (fromBank.getCoins().compareTo(amount) < 0) {
            return false;
        }

        fromBank.setCoins(fromBank.getCoins().subtract(amount));
        tomBank.setCoins(tomBank.getCoins().add(amount));

        transferLimitService.recordTransfer(fromUser, amount);

        userService.update(fromUser);
        userService.update(toUser);

        return true;
    }
}

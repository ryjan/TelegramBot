package org.ryjan.telegram.commands.users.user.transfers;

import org.ryjan.telegram.model.users.BankDatabase;
import org.ryjan.telegram.model.users.UserDatabase;
import org.ryjan.telegram.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransferService {
    @Autowired
    private UserService userService;
    @Autowired
    private TransferLimitService transferLimitService;

    public String transferCoins(String fromUsername, String toUsername, BigDecimal amount) {
        UserDatabase fromUser = userService.findUser(fromUsername);
        UserDatabase toUser = userService.findUser(toUsername);

        if (fromUser == null || toUser == null) {
            return fromUser == null ? "FromUser пуст!" : "Пользователь не найден!";
        }

        if (!transferLimitService.canTransfer(fromUser, amount)) {
            return "!canTransfer";
        }

        BankDatabase fromBank = fromUser.getBank();
        BankDatabase toBank = toUser.getBank();

        if (fromBank.getCoins().compareTo(amount) < 0) {
            return "У вас недостаточно монет 😥";
        }

        fromBank.setCoins(fromBank.getCoins().subtract(amount));
        toBank.setCoins(toBank.getCoins().add(amount));

        transferLimitService.recordTransfer(fromUser, amount); //

        userService.update(fromUser);
        userService.update(toUser);

        return "Успешно!";
    }
}

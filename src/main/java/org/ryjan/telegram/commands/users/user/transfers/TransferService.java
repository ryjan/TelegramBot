package org.ryjan.telegram.commands.users.user.transfers;

import org.ryjan.telegram.model.users.Bank;
import org.ryjan.telegram.model.users.User;
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
        User fromUser = userService.findUser(fromUsername);
        User toUser = userService.findUser(toUsername);

        if (fromUser == null || toUser == null) {
            return fromUser == null ? "FromUser пуст!" : "Пользователь не найден!";
        }

        if (!transferLimitService.canTransfer(fromUser, amount)) {
            return "!canTransfer";
        }

        Bank fromBank = fromUser.getBanks();
        Bank toBank = toUser.getBanks();

        if (fromBank.getCoins().compareTo(amount) < 0) {
            return "У вас недостаточно монет 😥";
        }

        fromBank.setCoins(fromBank.getCoins().subtract(amount));
        toBank.setCoins(toBank.getCoins().add(amount));

        transferLimitService.recordTransfer(fromUser, amount); //

        userService.save(fromUser);
        userService.save(toUser);

        return "Успешно!";
    }
}

package org.ryjan.telegram.commands;

import org.ryjan.telegram.handler.ButtonCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.BankDatabase;
import org.ryjan.telegram.model.UserDatabase;
import org.ryjan.telegram.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.math.BigDecimal;

@Component
public class SetCoins extends BaseCommand {
    @Autowired
    UserService userService;

    public SetCoins() {
        super("/setcoins", "Установить монеты пользователю");
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, ButtonCommandHandler buttonCommandHandler) {
        SendMessage message = createSendMessage(chatId);

        String[] parts = getParts(getCommandName(), 2);

        if (parts.length != 2 || !parts[0].startsWith("@")) {
            message.setText(wrongCommand("@Ryjan4ik 123"));
            sendMessageForCommand(bot, message);
            return;
        }

        String username = parts[0];
        String amountString = parts[1];
        UserDatabase userDatabase = findUserDatabase(username.substring(1));
        if (userDatabase == null) {
            message.setText(userNotFound(username));
            sendMessageForCommand(bot, message);
            return;
        }

        try {
            BigDecimal amount = new BigDecimal(amountString);
            BankDatabase bankDatabase = userDatabase.getBank();
            bankDatabase.setCoins(amount);
            userService.update(userDatabase);
            message.setText("Успешно🤙\nПользователю " + username + " выставлено " + amount + "🪙");
        } catch (IllegalArgumentException e) {
            message.setText(invalidAmount(amountString));
        }
        sendMessageForCommand(bot, message);
    }
}

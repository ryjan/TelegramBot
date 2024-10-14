package org.ryjan.telegram.commands.users.owner;

import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.users.BankDatabase;
import org.ryjan.telegram.model.users.UserDatabase;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.math.BigDecimal;

@Component
public class SetCoins extends BaseCommand {

    public SetCoins() {
        super("/setcoins", "Установить монеты пользователю", GroupPermissions.CREATOR);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler commandHandler) {
        SendMessage message = createSendMessage(chatId);

        String[] parts = getParts(getCommandName(), 2);

        if (parts.length != 2 || !parts[0].startsWith("@")) {
            message.setText("❌Команда введена неверно. Пример:\n@Ryjan4ik 123");
            sendMessageForCommand(bot, message);
            return;
        }

        String username = parts[0];
        String amountString = parts[1];
        UserDatabase userDatabase = userService.findUser(username.substring(1)); // оптимизировать под redis
        if (userDatabase == null) {
            message.setText("👾Пользователь не найден" + username);
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
            message.setText("❌Неверно введена сумма" + amountString);
        }
        sendMessageForCommand(bot, message);
    }
}

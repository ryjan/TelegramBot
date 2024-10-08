package org.ryjan.telegram.commands.users.owner;

import org.ryjan.telegram.commands.users.user.UserGroup;
import org.ryjan.telegram.commands.users.utils.BaseUserCommand;
import org.ryjan.telegram.handler.UserCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.users.BankDatabase;
import org.ryjan.telegram.model.users.UserDatabase;
import org.ryjan.telegram.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.math.BigDecimal;

@Component
public class SetCoins extends BaseUserCommand {

    @Autowired
    UserService userService;

    public SetCoins() {
        super("/setcoins", "Установить монеты пользователю");
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, UserCommandHandler userCommandHandler) {
        SendMessage message = createSendMessage(chatId);
        UserDatabase fromUser = userService.findUser(getUpdate().getMessage().getFrom().getId());

        if (!fromUser.isOwner()) {
            message.setText(noPermission(getCommandName(), UserGroup.OWNER));
            sendMessageForCommand(bot, message);
            return;
        }

        String[] parts = getParts(getCommandName(), 2);

        if (parts.length != 2 || !parts[0].startsWith("@")) {
            message.setText(wrongCommand("@Ryjan4ik 123", getCommandName()));
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

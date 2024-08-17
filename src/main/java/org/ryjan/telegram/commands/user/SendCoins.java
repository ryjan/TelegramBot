package org.ryjan.telegram.commands.user;

import org.ryjan.telegram.commands.BaseCommand;
import org.ryjan.telegram.commands.interfaces.IBotCommand;
import org.ryjan.telegram.commands.user.transfers.TransferService;
import org.ryjan.telegram.database.UserDatabase;
import org.ryjan.telegram.handler.ButtonCommandHandler;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.services.UserService;
import org.ryjan.telegram.utils.UpdateContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.io.IOException;
import java.math.BigDecimal;

public class SendCoins extends BaseCommand {
    UserService userService = super.userService;

    @Override
    public void executeCommand(String chatId, BotMain bot, ButtonCommandHandler buttonCommandHandler) {
        Update update = UpdateContext.getInstance().getUpdate();
        TransferService transferService = new TransferService();
        UserDatabase fromUser = getFromUserDatabase();
        UserDatabase toUser;
        SendMessage message = createSendMessage();

        String[] parts = getParts(getCommand(), 2);
        if (parts.length != 2 || !parts[0].startsWith("@")) {
            message.setText("Введена неверная команда!\nПример: /sendcoins @Ryjan4ik 999");
            sendMessageForCommand(bot, message);
            return;
        }
        String username = parts[0];
        String amountString = parts[1];
        toUser = getToUserDatabase(username.substring(1));
        if (toUser == null) {
            message.setText(userNotFound(username));
            sendMessageForCommand(bot, message);
            return;
        }

        try {
            BigDecimal amount = new BigDecimal(amountString);
            String result = transferService.transferCoins(getUsername(), toUser.getUserTag(), new BigDecimal(30)) ? "Операция прошла успешно🤙" : "Что-то пошло не так(";
            message.setText(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sendMessageForCommand(bot, message);
    }

    @Override
    public String getCommand() {
        return "/sendcoins";
    }

    @Override
    public String getDescription() {
        return "Поделиться монетами🪙";
    }
}

package org.ryjan.telegram.commands.user;

import org.ryjan.telegram.commands.BaseCommand;
import org.ryjan.telegram.commands.user.transfers.TransferService;
import org.ryjan.telegram.model.UserDatabase;
import org.ryjan.telegram.handler.ButtonCommandHandler;
import org.ryjan.telegram.BotMain;
import org.ryjan.telegram.services.BotService;
import org.ryjan.telegram.utils.UpdateContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.math.BigDecimal;

@Component
public class SendCoins extends BaseCommand {
    @Autowired
    TransferService transferService;


    public SendCoins() {
        super("/sendcoins", "Отправить пользователю свои монеты🪙");
    }

    @Override
    public void executeCommand(String chatId, BotService bot, ButtonCommandHandler buttonCommandHandler) {
        Update update = UpdateContext.getInstance().getUpdate();
        UserDatabase fromUser = getFromUserDatabase();
        UserDatabase toUser;
        SendMessage message = createSendMessage();

        String[] parts = getParts(getCommandName(), 2);
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
}
